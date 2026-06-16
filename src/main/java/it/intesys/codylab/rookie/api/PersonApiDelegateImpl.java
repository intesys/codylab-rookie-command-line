package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.service.PersonService;
import it.intesys.codylab.rookie.web.api.PersonApiDelegate;
import it.intesys.codylab.rookie.web.api.model.PersonApiDTO;
import it.intesys.codylab.rookie.web.api.model.PersonFilterApiDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class PersonApiDelegateImpl implements PersonApiDelegate {
    final PersonService personService;

    public PersonApiDelegateImpl(PersonService personService) {
        this.personService = personService;
        Person.incrementLastId(100);
    }

    @Override
    public ResponseEntity<PersonApiDTO> getPerson(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<PersonApiDTO> createPerson(PersonApiDTO personApiDTO) {
        try {
            Person person = new Person();


            person.id = personApiDTO.getId();
            person.name = personApiDTO.getName();
            person.surname = personApiDTO.getSurname();
            person.registrationDate = personApiDTO.getRegistrationDate();

            personService.process(List.of (person));

            return ResponseEntity.ok(personApiDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Void> deletePerson(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<PersonApiDTO>> getListPerson(Integer page, Integer size, String sort, PersonFilterApiDTO personFilterApiDTO) {
        return null;
    }
}
