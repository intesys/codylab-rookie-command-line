package it.intesys.codylab.rookie;

import it.intesys.codylab.rookie.commandline.ServerDiRete;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class CodylabRookie {
    public static void main(String[] args) throws IOException {
        SpringApplication springApplication = new SpringApplication(CodylabRookie.class);
        ConfigurableApplicationContext context = springApplication.run(args);

        ServerDiRete serverDiRete = context.getBean(ServerDiRete.class);

        System.out.printf("Bean: %s\n", serverDiRete);
        serverDiRete.process ();
    }
}
