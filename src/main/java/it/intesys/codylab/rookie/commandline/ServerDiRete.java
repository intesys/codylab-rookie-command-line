package it.intesys.codylab.rookie.commandline;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerDiRete {
    static int port;
    static int numberOfClients = 0;

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
        for (;;) try (Socket socket = serverSocket.accept()) {
            System.out.printf("Connessione n. %d ricevuta da %s\n", ++numberOfClients, socket.getInetAddress());
            process(socket);
        }
    }

    private static void process(Socket socket) throws IOException {
        List<Person> persone = readInput(socket);
        boolean outcome = process(persone);
        System.out.printf("Risultato: %b\n", outcome);
    }


    private static boolean process(List<Person> persons) {
        for (Person person : persons) {
            System.out.println(person.toString(true));
        }
        return true;
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
        for (int i = 0; i < arguments.length; i++) {
            switch (arguments[i]) {
                case "--port":
                    port = Integer.parseInt(arguments[++i]);
                    break;
                default:
                    System.err.println("Unknown argument: " + arguments[i]);
                    System.exit(1);
            }
        }

        if (port == 0)
            argumentsError ("port");
    }

    private static void argumentsError(String server) {
        System.err.printf("%s obbligatorio", server);
        System.exit(2);
    }


}
