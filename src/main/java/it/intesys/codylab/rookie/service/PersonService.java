package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;

import java.io.IOException;
import java.util.List;

public interface PersonService extends RookieService {
    void createPerson(Person person);
    void updatePerson(Person person);
    void deletePerson(Person person);
}
