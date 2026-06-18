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
        Person person = personService.findPerson(id);
        PersonApiDTO personApiDTO = map (person);
        return ResponseEntity.ok(personApiDTO);
    }

    private PersonApiDTO map(Person person) {
        PersonApiDTO personApiDTO = new PersonApiDTO();
        personApiDTO.setId(person.id);
        personApiDTO.setId(person.id);
        personApiDTO.setName(person.name);
        personApiDTO.setSurname(person.surname);
        personApiDTO.setRegistrationDate(person.registrationDate);
        return personApiDTO;
    }

    @Override
    public ResponseEntity<PersonApiDTO> createPerson(PersonApiDTO personApiDTO) {
        Person person = map(null, personApiDTO);

        person = personService.createPerson(person);

        personApiDTO.setId(person.id);
        return ResponseEntity.ok(personApiDTO);
    }

    private static Person map(Long id, PersonApiDTO personApiDTO) {
        Person person = new Person();

        person.id = id != null? id : personApiDTO.getId();
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
        Person person = personService.findPerson (id);
        personService.deletePerson(person);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<PersonApiDTO>> findPeople(Integer page, Integer size, String sort, PersonFilterApiDTO personFilterApiDTO) {
        return null;
    }

}
