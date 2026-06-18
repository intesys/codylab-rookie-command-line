package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.commandline.Servizio;
import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Servizio
@Service
public class PersonServiceImpl implements PersonService {
    PersonRepository personRepository;

    public PersonServiceImpl() {
    }

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void process(List<Person> persons) throws IOException {
        for (Person person : persons) {
            try {
                process(person);
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
    }

    private void process(Person person) throws SQLException {
        personRepository.save(person);
    }
}
