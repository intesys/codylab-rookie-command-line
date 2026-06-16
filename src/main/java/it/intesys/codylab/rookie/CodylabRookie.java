package it.intesys.codylab.rookie;

import it.intesys.codylab.rookie.commandline.ServerDiRete;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class CodylabRookie {
    public static void main(String[] args) throws IOException {
        new SpringApplication(CodylabRookie.class)
                .run(args);
    }
}
