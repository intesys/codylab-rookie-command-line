package it.intesys.codylab.rookie.commandline.frontend;

import it.intesys.codylab.rookie.commandline.Person;
import it.intesys.codylab.rookie.commandline.RigaDiComando;

import java.io.IOException;
import java.util.List;

public class ClienteDiRete {
    private static List<Person> persone;
    ClienteServizioPersone clienteServizioPersone;

    static void main(String [] arguments) throws IOException {
        ClienteDiRete clienteDiRete = read (arguments);
        clienteDiRete.process ();
    }

    private void process() throws IOException {
        clienteServizioPersone.process(persone);
    }


    private static ClienteDiRete read(String[] arguments) {
        ClienteDiRete clienteDiRete = new ClienteDiRete();

        String server = null;
        int port = 0;

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

        clienteDiRete.clienteServizioPersone = new ClienteServizioPersone(server, port);
        return clienteDiRete;

    }

    private static void argumentsError(String server) {
        System.err.printf("%s obbligatorio", server);
        System.exit(2);
    }
}
