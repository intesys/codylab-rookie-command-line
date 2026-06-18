package it.intesys.codylab.rookie.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Person {
    private static long lastId;

    @Id
    public Long id;
    public String name;
    public String surname;
    public Instant registrationDate;

    public Person () {
        lastId = lastId + 1;
        this.id = lastId;
        registrationDate = java.time.Instant.now();
    }

    public Person (String name, String surname) {
        this ();
        this.name = name;
        this.surname = surname;
    }

    public Person (String name, String surname, Instant registrationDate) {
        this (name, surname);
        this.registrationDate = registrationDate;
    }

    public String toString(boolean longFormat) {
        if (longFormat) {
            return "\nPerson (id: " + id + ",\n" +
                "name: " + name + ",\n" +
                "surname: " + surname + ",\n" +
                "registrationDate: " + registrationDate + ")\n";
        } else {
            return "Person (id: " + id  + ")";
        }
    }

    public static void resetLastId () {
        lastId = 0;
    }

    public static void incrementLastId (int lastIdIncrement) {
        lastId += lastIdIncrement;
    }

}
