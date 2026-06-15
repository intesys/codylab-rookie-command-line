package it.intesys.codylab.rookie.commandline;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerDiRete {
    int port;
    int numberOfClients = 0;
    ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3 / 2);
    List<Object> servizi;

    static void main(String[] arguments) throws Exception {
        ServerDiRete serverDiRete = read(arguments);
        System.out.printf("La porta è %d\n", serverDiRete.port);
        serverDiRete.process();
    }

    private void process() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Pronto per ricevere connessioni sulla porta %d\n", port);
            process(serverSocket);
        }
    }

    private void process(ServerSocket serverSocket) throws IOException {
        for (;;) {
            Socket socket = serverSocket.accept();
            System.out.printf("Connessione n. %d ricevuta da %s\n", ++numberOfClients, socket.getInetAddress());
            threadPool.submit(new ClientProcessing(socket));
        }
    }

    private void process(Socket socket) throws IOException, SQLException, InvocationTargetException, IllegalAccessException {
        try (socket) {
            List<Person> persone = readInput(socket);
            String outcome = dispatch(persone);
            writeOutcome(socket, outcome);
        }
    }

    public String dispatch(Object argument) throws IllegalAccessException {
        for (Object servizio : servizi) {
            Method[] methods = servizio.getClass().getMethods();
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(argument.getClass())) {
                    try {
                        method.invoke(servizio, argument);
                        System.out.printf("Metodo %s invocato sul servizio %s\n", method.getName (), servizio.getClass().getSimpleName());
                        return "OK";
                    } catch (InvocationTargetException e) {
                        Throwable targetException = e.getTargetException();
                        System.err.printf("Invocazione fallita del metodo %s del servizio %s\n", method.getName (), servizio.getClass().getSimpleName());
                        System.err.println(targetException.getMessage());
                        System.err.flush();
                        return targetException.getMessage();
                    }
                }
            }
        }
        System.err.println("Servizio non trovato per process(List<Person>)");
        return "KO";
    }

    private void writeOutcome(Socket socket, String outcome) throws IOException {
        Writer writer = new OutputStreamWriter(socket.getOutputStream());
        writer.write(outcome);
        writer.flush();
    }




    private List<Person> readInput(Socket socket) throws IOException {
        String[] arguments = readInput(socket.getInputStream());
        return RigaDiComando.readArguments(arguments);
    }

    private String[] readInput(InputStream inputStream) throws IOException {
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

    private void addArgument(StringBuilder stringBuilder, List<String> arguments) {
        String argument = stringBuilder.toString();
        arguments.add(argument);
        stringBuilder.setLength(0);
    }


    private static ServerDiRete read(String[] arguments) throws Exception {
        ServerDiRete serverDiRete = new ServerDiRete();

        String pgHost = null, pgUsername = null, pgPassword = null, pgDatabase = null;
        int pgPort = 0;

        for (int i = 0; i < arguments.length; i++) {
            switch (arguments[i]) {
                case "--port":
                    serverDiRete.port = Integer.parseInt(arguments[++i]);
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

        if (serverDiRete.port == 0)
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

        serverDiRete.discoverServices(pgDataSource);

        return serverDiRete;
    }

    private static void argumentsError(String server) {
        System.err.printf("%s obbligatorio", server);
        System.exit(2);
    }

    class ClientProcessing implements Runnable {
        Socket socket;

        ClientProcessing (Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                process(socket);
            }  catch (IOException | SQLException | InvocationTargetException | IllegalAccessException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    void discoverServices(DataSource dataSource) throws Exception {
        servizi = new ArrayList<>();

        String packageName = "it.intesys.codylab.rookie.commandline";
        String path = packageName.replace('.', '/');

        URL url = ClassLoader.getSystemClassLoader().getResource(path);
        File dir = new File(url.toURI());

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                Class<?> cls = Class.forName(className);

                if (cls.isAnnotationPresent(Servizio.class)) {
                    System.out.println(cls.getName() + " ha l'annotazione @Servizio");
                    Constructor<?> constructor = cls.getConstructor(DataSource.class);
                    Object servizio = constructor.newInstance(dataSource);
                    servizi.add(servizio);
                }
            }
        }
    }
}
