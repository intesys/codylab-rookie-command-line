package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;

public interface PersonService extends RookieService {
    Person createPerson(Person person);
    void updatePerson(Person person);
    void deletePerson(Person person);
}
