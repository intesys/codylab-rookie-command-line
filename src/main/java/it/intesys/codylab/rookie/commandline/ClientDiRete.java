package it.intesys.codylab.rookie.commandline;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class ClientDiRete {
    static String server;
    static int port;
    private static ArrayList<Person> persone;

    static void main(String [] arguments) throws IOException {
        read (arguments);
        process(server, port);
    }

    private static void process(String localhost, int port) throws IOException {
        try (Socket socket = new Socket()) {
            socket.connect(new java.net.InetSocketAddress(localhost, port));

            process(socket);
        }
    }

    private static void process(Socket socket) throws IOException {
        Writer writer = new OutputStreamWriter(socket.getOutputStream());
        for (int i = 0; i < persone.size(); i++) {
            write (persone.get(i), writer);
        }
    }

    private static void write(Person person, Writer writer) throws IOException {
        writer.write("--person");
        writer.write ("-name");
        writer.write (person.name);
        writer.write ("-surname");
        writer.write (person.surname);
        writer.write ("-registrationDate");
        writer.write (RigaDiComando.instantFormatter.format(person.registrationDate));
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
                    persone = RigaDiComando.readInput(arguments);
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
