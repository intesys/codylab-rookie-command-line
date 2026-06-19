package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.Person;
import it.intesys.codylab.rookie.web.api.model.PersonApiDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonaMapper {
    Person toPerson (PersonApiDTO personApiDTO);
    PersonApiDTO toDTO (Person person);
}
