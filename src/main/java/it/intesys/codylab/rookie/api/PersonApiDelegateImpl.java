package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.service.PersonService;
import it.intesys.codylab.rookie.web.api.PersonApiDelegate;
import it.intesys.codylab.rookie.web.api.model.PersonApiDTO;
import it.intesys.codylab.rookie.web.api.model.PersonFilterApiDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonApiDelegateImpl implements PersonApiDelegate {
    final PersonService personService;

    public PersonApiDelegateImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public ResponseEntity<PersonApiDTO> getPerson(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<PersonApiDTO> createPerson(PersonApiDTO personApiDTO) {
        Person person = map(null, personApiDTO);

        personService.createPerson(person);

        return ResponseEntity.ok(personApiDTO);
    }

    private static Person map(Long id, PersonApiDTO personApiDTO) {
        Person person = new Person();

        //person.id = id;
        person.name = personApiDTO.getName();
        person.surname = personApiDTO.getSurname();
        person.registrationDate = personApiDTO.getRegistrationDate();
        return person;
    }

    @Override
    public ResponseEntity<Void> updatePerson(Long id, PersonApiDTO personApiDTO) {
        Person person = map(id, personApiDTO);

        personService.updatePerson(person);

        return ResponseEntity.ok().build();

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
