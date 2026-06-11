package it.intesys.codylab.rookie.commandline;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientDiRete {
    static String server;
    static int port;
    private static List<Person> persone;

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
        send(socket);
        socket.shutdownOutput();
        String outcome = receiveOutcome(socket);
        if (outcome.isEmpty()) {
            System.err.println("No result");
        } else if (!outcome.equalsIgnoreCase("OK"))
            System.err.println(outcome);
    }

    private static void send(Socket socket) throws IOException {
        Writer writer = new OutputStreamWriter(socket.getOutputStream());
        for (int i = 0; i < persone.size(); i++) {
            write(persone.get(i), writer);
        }
    }

    private static String receiveOutcome(Socket socket) throws IOException {
        Reader reader = new InputStreamReader(socket.getInputStream());
        return receiveOutcome(reader);
    }

    private static String receiveOutcome(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int chAsInt;
        char ch;
        while ((chAsInt = reader.read()) != -1) {
            ch = (char) chAsInt;
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }

    private static void write(Person person, Writer writer) throws IOException {
        System.out.printf("Sending %s", person.toString(true));
        writer.write('"');
        writer.write("--person");
        writer.write('"');
        writer.write(' ');
        writer.write ("-name");
        writer.write(' ');
        writer.write (person.name);
        writer.write(' ');
        writer.write ("-surname");
        writer.write(' ');
        writer.write (person.surname);
        writer.write(' ');
        writer.write ("-registrationDate");
        writer.write(' ');
        writer.write('"');
        writer.write (RigaDiComando.formatInstant(person.registrationDate));
        writer.write('"');
        writer.write(' ');
        writer.flush();
        System.out.println("Sent");
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
                    persone = RigaDiComando.readArguments(arguments);
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
