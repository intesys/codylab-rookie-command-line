package it.intesys.codylab.rookie.commandline;

public class Main {
    void main() throws InterruptedException {
        Person.incrementLastId(7);

        Person p = new Person();
        p.name = "Samuele";
        p.surname = "Bortolotti";

        System.out.println(p.toString(true));

        Thread.sleep(100);

        Person p2 = new Person();
        p2.id = 2;
        p2.name = "Davide";
        p2.surname = "Crema";
        p2.registrationDate = java.time.Instant.now();

        System.out.println(p2.toString(true));

        Thread.sleep(100);

        Person p3 = new Person("Avneet", "Kaur");
        p3.registrationDate = java.time.Instant.now();

        System.out.println(p3.toString(true));

        Thread.sleep(100);

        Person p4 = new Person("Chiara", "Salvaro",  java.time.Instant.now());

        System.out.println(p4.toString(true));

    }
}
