package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;

import java.io.IOException;
import java.util.List;

public interface PersonService {
    void process(List<Person> person) throws IOException;
}
