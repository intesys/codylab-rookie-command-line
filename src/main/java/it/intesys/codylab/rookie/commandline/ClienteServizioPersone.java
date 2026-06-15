package it.intesys.codylab.rookie.commandline;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClienteServizioPersone {
    final String server;
    final int port;
    Socket socket;
    List<Person> persone;

    public ClienteServizioPersone(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void process(List<Person> persone) throws IOException {
        this.persone = persone;
        try (Socket socket = new Socket()) {
            this.socket = socket;
            final int oneMinute = 1000 * 60 * 10;
            socket.connect(new java.net.InetSocketAddress(server, port), oneMinute);
            socket.setSoTimeout(oneMinute);
            process();
        }
    }

    private void process() throws IOException {
        send();
        socket.shutdownOutput();
        String outcome = receiveOutcome();
        if (outcome.isEmpty()) {
            System.err.println("No result");
        } else if (!outcome.equalsIgnoreCase("OK"))
            System.err.println(outcome);
    }

    private void send() throws IOException {
        Writer writer = new OutputStreamWriter(socket.getOutputStream());
        for (int i = 0; i < persone.size(); i++) {
            write(persone.get(i), writer);
        }
    }

    private String receiveOutcome() throws IOException {
        Reader reader = new InputStreamReader(socket.getInputStream());
        return receiveOutcome(reader);
    }

    private String receiveOutcome(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int chAsInt;
        char ch;
        while ((chAsInt = reader.read()) != -1) {
            ch = (char) chAsInt;
            stringBuilder.append(ch);
        }
        return stringBuilder.toString();
    }

    private void write(Person person, Writer writer) throws IOException {
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
}
