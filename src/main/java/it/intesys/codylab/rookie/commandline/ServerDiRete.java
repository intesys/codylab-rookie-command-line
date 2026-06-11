package it.intesys.codylab.rookie.commandline;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
