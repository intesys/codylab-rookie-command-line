package it.intesys.codylab.rookie;

import it.intesys.codylab.rookie.service.PersonService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CodylabRookie {
    final PersonService personService;

    public CodylabRookie(PersonService personService) {
        this.personService = personService;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CodylabRookie.class);
        ConfigurableApplicationContext context = springApplication.run(args);
        CodylabRookie codylabRookie = context.getBean(CodylabRookie.class);
        System.out.printf("Bean: %s\n", codylabRookie);
    }
}
