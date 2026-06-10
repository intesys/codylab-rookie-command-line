package it.intesys.codylab.rookie.commandline;

import java.time.Instant;

public class Person {
    private static int lastId;

    int id;
    String name;
    String surname;
    Instant registrationDate;

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

    String toString(boolean longFormat) {
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
