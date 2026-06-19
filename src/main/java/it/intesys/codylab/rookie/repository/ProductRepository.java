package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query(value = """
        select id, name, quantity from product
        where (:id is null
           or id = :id)
        and (:text is null
            or lower (name) like concat ('%', lower (:text), '%'))
    """, nativeQuery = true)
    List<Product> findProducts(@Param("id") Long id, @Param("text") String text, Pageable pageable);
}
