package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.web.api.PersonApiDelegate;
import it.intesys.codylab.rookie.web.api.model.PersonApiDTO;
import it.intesys.codylab.rookie.web.api.model.PersonFilterApiDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonApiDelegateImpl implements PersonApiDelegate {
    @Override
    public ResponseEntity<PersonApiDTO> getPerson(Long id) {
        return PersonApiDelegate.super.getPerson(id);
    }

    @Override
    public ResponseEntity<PersonApiDTO> createPerson(PersonApiDTO personApiDTO) {
        return PersonApiDelegate.super.createPerson(personApiDTO);
    }

    @Override
    public ResponseEntity<Void> deletePerson(Long id) {
        return PersonApiDelegate.super.deletePerson(id);
    }

    @Override
    public ResponseEntity<List<PersonApiDTO>> getListPerson(Integer page, Integer size, String sort, PersonFilterApiDTO personFilterApiDTO) {
        return PersonApiDelegate.super.getListPerson(page, size, sort, personFilterApiDTO);
    }
}
