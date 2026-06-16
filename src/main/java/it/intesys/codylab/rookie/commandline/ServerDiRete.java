package it.intesys.codylab.rookie.commandline;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.service.RookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@ConfigurationProperties("server")
public class ServerDiRete {
    int port;
    int numberOfClients = 0;
    ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3 / 2);

    List<RookieService> servizi;

    ServerDiRete() {
    }

    @Autowired
    ServerDiRete(List<RookieService> servizi) {
        this.servizi = servizi;
    }


    public void process() throws IOException {
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
