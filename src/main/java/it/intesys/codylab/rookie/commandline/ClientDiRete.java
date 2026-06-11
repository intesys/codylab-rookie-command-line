package it.intesys.codylab.rookie.commandline;

import java.io.IOException;
import java.net.Socket;

public class ClientDiRete {
    static String server;
    static int port;

    static void main(String [] arguments) throws IOException {
        read (arguments);
        process(server, port);
    }

    private static void process(String localhost, int port) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(localhost, port));
        }
    }

    private static void read(String[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            switch (arguments[i]) {
                case "--server":
                    server = arguments[++i];
                    break;
                case "--port":
                    port = Integer.parseInt(arguments[++i]);
                    break;
                default:
                    System.err.println("Unknown argument: " + arguments[i]);
                    System.exit(1);
            }
        }

        if (server == null)
            argumentsError ("server");

        if (port == 0)
            argumentsError ("port");

    }

    private static void argumentsError(String server) {
        System.err.printf("%s obbligatorio", server);
        System.exit(2);
    }
}
