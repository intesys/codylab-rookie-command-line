package it.intesys.codylab.rookie.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public Integer quantity;


    public String toString() {
        return "\nProduct (id: " + id + ",\n" +
            "name: " + name + ",\n" +
            "quantity: " + quantity + ")\n";
    }

}
