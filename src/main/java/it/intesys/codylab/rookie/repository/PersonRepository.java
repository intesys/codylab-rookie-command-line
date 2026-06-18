package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
}
