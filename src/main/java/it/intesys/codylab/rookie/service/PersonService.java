package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService extends RookieService {
    Person createPerson(Person person);
    void updatePerson(Person person);
    void deletePerson(Person person);

    Person findPerson(Long id);

    List<Person> findPeople(Long id, String text, Pageable pageable);
}
