package it.intesys.codylab.rookie.commandline;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDiRete {
    static int port;
    static int numberOfClients = 0;
    static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3 / 2);
    static DataSource datasource;

    static void main(String[] arguments) throws IOException {
        read(arguments);
        System.out.printf("La porta è %d\n", port);
        process ();
    }

    private static void process() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Pronto per ricevere connessioni sulla porta %d\n", port);
            process(serverSocket);
        }
    }

    private static void process(ServerSocket serverSocket) throws IOException {
        for (;;) {
            Socket socket = serverSocket.accept();
            System.out.printf("Connessione n. %d ricevuta da %s\n", ++numberOfClients, socket.getInetAddress());
            threadPool.submit(new ClientProcessing(socket));
        }
    }

    private static void process(Socket socket) throws IOException, SQLException {
        try (socket) {
            List<Person> persone = readInput(socket);
            String outcome = randomOutcome();
            System.out.printf("Risultato: %s\n", outcome);
            if (outcome.equalsIgnoreCase("OK"))
                process(persone);
            writeOutcome(socket, outcome);
        }
    }

    private static void writeOutcome(Socket socket, String outcome) throws IOException {
        Writer writer = new OutputStreamWriter(socket.getOutputStream());
        writer.write(outcome);
        writer.flush();
    }

    private static String randomOutcome() {
        int timeModuloDue = (int) System.currentTimeMillis() % 2;
        String outcome;
        switch (timeModuloDue) {
            case 0:
                outcome = "OK";
                break;
            default:
                outcome = "ERROR";
        }
        return outcome;
    }


    private static void process(List<Person> persons) throws SQLException {
        for (Person person : persons) {
            process(person);
        }
    }

    private static void process(Person person) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            String sql = """
                            INSERT INTO person 
                                (id, name, surname, registration_date)
                            VALUES 
                                (?, ?, ?, ?)
                    """;
            try (PreparedStatement query = connection.prepareStatement(sql)) {
                query.setLong(1, person.id);
                query.setString(2, person.name);
                query.setString(3, person.surname);
                query.setTimestamp(4, Timestamp.from(person.registrationDate));

                int rowUpdated = query.executeUpdate();
                if (rowUpdated != 1)
                    System.err.printf("Error inserting person %s: %d rows updated\n", person.toString(true), rowUpdated);
            }
        }
    }

    private static List<Person> readInput(Socket socket) throws IOException {
        String[] arguments = readInput(socket.getInputStream());
        return RigaDiComando.readArguments(arguments);
    }

    private static String[] readInput(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream);
        List<String> argumentsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        int chAsInt;
        char ch;
        boolean insideQuote = false;
        while ((chAsInt = reader.read()) != -1) {
            ch = (char) chAsInt;
            switch (ch) {
                case ' ':
                    if (insideQuote) {
                        stringBuilder.append(ch);
                    } else {
                        addArgument(stringBuilder, argumentsList);
                    }
                    break;
                case '"':
                    if (insideQuote) {
                        addArgument(stringBuilder, argumentsList);
                    }
                    insideQuote = !insideQuote;
                    break;
                default:
                    stringBuilder.append(ch);
            }
        }

        return argumentsList.toArray(new String[0]);
    }

    private static void addArgument(StringBuilder stringBuilder, List<String> arguments) {
        String argument = stringBuilder.toString();
        arguments.add(argument);
        stringBuilder.setLength(0);
    }


    private static void read(String[] arguments) {
        String pgHost = null, pgUsername = null, pgPassword = null, pgDatabase = null;
        int pgPort = 0;

        for (int i = 0; i < arguments.length; i++) {
            switch (arguments[i]) {
                case "--port":
                    port = Integer.parseInt(arguments[++i]);
                    break;
                case "--pg-host":
                    pgHost = arguments[++i];
                    break;
                case "--pg-port":
                    pgPort = Integer.parseInt(arguments[++i]);
                    break;
                case "--pg-username":
                    pgUsername = arguments[++i];
                    break;
                case "--pg-password":
                    pgPassword = arguments[++i];
                    break;
                case "--pg-database":
                    pgDatabase = arguments[++i];
                    break;
                default:
                    System.err.println("Unknown argument: " + arguments[i]);
                    System.exit(1);
            }
        }

        if (port == 0)
            argumentsError ("--port");
        if (pgHost == null)
            argumentsError ("--pg-host");
        if (pgPort == 0)
            argumentsError ("--pg-port");
        if (pgUsername == null)
            argumentsError ("--pg-username");
        if (pgPassword == null)
            argumentsError ("--pg-password");
        if (pgDatabase == null)
            argumentsError ("--pg-database");

        PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setServerName(pgHost);
        pgDataSource.setPortNumber(pgPort);
        pgDataSource.setUser(pgUsername);
        pgDataSource.setPassword(pgPassword);
        pgDataSource.setDatabaseName(pgDatabase);

        datasource = pgDataSource;
    }

    private static void argumentsError(String server) {
        System.err.printf("%s obbligatorio", server);
        System.exit(2);
    }

    static class ClientProcessing implements Runnable {
        Socket socket;

        ClientProcessing (Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                process(socket);
            }  catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
