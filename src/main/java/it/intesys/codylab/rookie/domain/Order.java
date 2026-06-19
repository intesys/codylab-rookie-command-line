package it.intesys.codylab.rookie.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "\"order\"")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name="date_created")
    public Instant createDate;

    @ManyToOne
    public Person person;
    @ManyToOne
    public Product product;

    public Integer quantity;

    public Order() {

    }

    public String toString() {
        return "\nOrder (id: " + id + ",\n" +
            "createDate: " + createDate + ",\n" +
            "person: " + person + ",\n" +
            "product: " + product + ",\n" +
            "quantity: " + quantity + ")\n";
    }
}
