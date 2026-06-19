package it.intesys.codylab.rookie.repository;

import it.intesys.codylab.rookie.domain.Order;
import it.intesys.codylab.rookie.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query(value = """
        select a.id, person_id, product_id, date_created, a.quantity
                from "order" as a
                            join person b on a.person_id = b.id
                            join product c on a.product_id = c.id
        where (:id is null
           or a.id = :id)
        and (:text is null
            or lower (b.name) like concat ('%', lower (:text), '%')
            or lower (b.surname) like concat ('%', lower (:text), '%')
            or lower (c.name) like concat ('%', lower (:text), '%'))
        and (cast (:createDateFrom as timestamp) is null
            or a.date_created >= :createDateFrom)
        and (cast (:createDateTo as timestamp) is null
            or a.date_created <= :createDateTo)
        """, nativeQuery = true)
    List<Order> findOrders(@Param("id") Long id,
                           @Param("text") String text,
                           @Param ("createDateFrom") Instant createDateFrom,
                           @Param ("createDateTo") Instant createDateTo);
}
