package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    PersonRepository personRepository;

    public PersonServiceImpl() {
    }

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public void createPerson(Person person) {
        if (person.id != null) {

        }
        personRepository.save(person);
    }

    @Override
    public void updatePerson(Person person) {
        if (person.id == null) {
            throw new Error("Cannot update person without id");
        }
        personRepository.save(person);
    }

    @Override
    public void deletePerson(Person person) {
        personRepository.delete(person);
    }
}
