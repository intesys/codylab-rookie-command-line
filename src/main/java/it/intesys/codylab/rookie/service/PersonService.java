package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;

import java.io.IOException;
import java.util.List;

public interface PersonService extends RookieService {
    void process(List<Person> person) throws IOException;
}
