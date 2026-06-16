package it.intesys.codylab.rookie.commandline;

import it.intesys.codylab.rookie.service.RookieService;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AvvioServerDiRete {

    static void main(String[] arguments) throws Exception {
        ServerDiRete serverDiRete = bootstrap(arguments);
        System.out.printf("La porta è %d\n", serverDiRete.port);
        serverDiRete.process();
    }

    private static ServerDiRete bootstrap(String[] arguments) throws Exception {
        int port = 0;
        String pgHost = null, pgUsername = null, pgPassword = null, pgDatabase = null;
        int pgPort = 0;

        for (int i = 0; i < arguments.length; i++) {
            switch (arguments[i]) {
                case "--port":
                    port = Integer.parseInt(arguments[++i]);
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

        if (port == 0)
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

        List<RookieService> rookieServices = discoverServices(pgDataSource);
        ServerDiRete serverDiRete = new ServerDiRete(rookieServices);
        serverDiRete.setPort(port);

        return serverDiRete;
    }

    private static void argumentsError(String server) {
        System.err.printf("%s obbligatorio", server);
        System.exit(2);
    }


    static List<RookieService> discoverServices(DataSource dataSource) throws Exception {
        List<RookieService> servizi = new ArrayList<>();

        String packageName = ".it.intesys.codylab.rookie.commandline.service";
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
                    RookieService servizio = (RookieService) constructor.newInstance(dataSource);
                    servizi.add(servizio);
                }
            }
        }

        return servizi;
    }

}
