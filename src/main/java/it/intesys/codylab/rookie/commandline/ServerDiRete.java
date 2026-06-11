package it.intesys.codylab.rookie.commandline;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.intesys.codylab.rookie.commandline.RigaDiComando.readInput;

public class ServerDiRete {
    static int port;

    static void main(String[] arguments) throws IOException {
        read(arguments);
        System.out.printf("La porta è %d\n", port);
        process ();
    }

    private static void process() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            process(serverSocket);
        }
    }

    private static void process(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        System.out.printf("Connessione ricevuta da %s", socket.getInetAddress());
        process (socket);
    }

    private static void process(Socket socket) throws IOException {
        Reader reader = new InputStreamReader(socket.getInputStream());
        List<String> arguments = new ArrayList<>();
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
                        addArgument(stringBuilder, arguments);
                    }
                    break;
                case '"':
                    if (insideQuote) {
                        addArgument(stringBuilder, arguments);
                    }
                    insideQuote = !insideQuote;
                    break;
                default:
                    stringBuilder.append(ch);
            }
        }
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
