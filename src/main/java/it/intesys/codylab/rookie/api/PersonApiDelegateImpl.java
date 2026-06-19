package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.mapper.PersonMapper;
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
    final PersonMapper personMapper;

    public PersonApiDelegateImpl(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @Override
    public ResponseEntity<PersonApiDTO> getPerson(Long id) {
        Person person = personService.findPerson(id);
        PersonApiDTO personApiDTO = personMapper.toDTO(person);
        return ResponseEntity.ok(personApiDTO);
    }

    @Override
    public ResponseEntity<PersonApiDTO> createPerson(PersonApiDTO personApiDTO) {
        Person person = map(null, personApiDTO);

        person = personService.createPerson(person);

        personApiDTO.setId(person.id);
        return ResponseEntity.ok(personApiDTO);
    }

    private Person map(Long id, PersonApiDTO personApiDTO) {
        Person person = personMapper.toPerson(personApiDTO);
        person.id = id != null? id : personApiDTO.getId();
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
    public ResponseEntity<List<PersonApiDTO>> findPeople(Integer page, Integer size, String sort, PersonFilterApiDTO filterDTO) {
        List<Person> people = personService.findPeople(filterDTO.getId(), filterDTO.getText());
        List<PersonApiDTO> peopleDTO = people.stream()
                .map(personMapper::toDTO)
                .toList();
        return ResponseEntity.ok(peopleDTO);

    }

}
