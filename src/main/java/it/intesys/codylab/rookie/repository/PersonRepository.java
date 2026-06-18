package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    @Query(value = """
        select id, name, surname, registration_date from person
        where (:id is null
           or id = :id)
        and (:text is null
            or name like concat ('%', :text, '%')
            or surname like concat ('%', :text, '%'))""", nativeQuery = true)
    List<Person> findPeople(@Param("id") Long id, @Param("text") String text);
}
