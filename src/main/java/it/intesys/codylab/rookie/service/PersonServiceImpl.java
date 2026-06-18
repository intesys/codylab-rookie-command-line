package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.exception.IdentityShouldNotBeSetOnCreateException;
import it.intesys.codylab.rookie.exception.MandatoryIdentityException;
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
    public Person createPerson(Person person) {
        if (person.id != null)
            throw new IdentityShouldNotBeSetOnCreateException(Person.class);

        return personRepository.save(person);
    }

    @Override
    public void updatePerson(Person person) {
        if (person.id == null)
            throw new MandatoryIdentityException(Person.class);

        personRepository.save(person);
    }

    @Override
    public void deletePerson(Person person) {
        if (person.id == null)
            throw new MandatoryIdentityException(Person.class);

        personRepository.delete(person);
    }
}
