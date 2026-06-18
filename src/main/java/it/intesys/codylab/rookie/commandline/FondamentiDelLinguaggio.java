package it.intesys.codylab.rookie.commandline;


public class FondamentiDelLinguaggio {
    public static void main(String [] arguments) throws InterruptedException {
        Person.incrementLastId(7);

        Person p = new Person();
        p.name = "Samuele";
        p.surname = "Bortolotti";

        println(p);

        Thread.sleep(100);

        Person p2 = new Person();
        p2.id = 2L;
        p2.name = "Davide";
        p2.surname = "Crema";
        p2.registrationDate = java.time.Instant.now();

        println(p2);

        Thread.sleep(100);

        Person p3 = new Person("Avneet", "Kaur");
        p3.registrationDate = java.time.Instant.now();

        println(p3);

        Thread.sleep(100);

        Person p4 = new Person("Chiara", "Salvaro",  java.time.Instant.now());

        println(p4);

    }

    private static void println(Person string) {
        try {
            stampa (string);
        } catch (NonMiPiaceLId3Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void stampa(Person person) throws NonMiPiaceLId3Exception {
        try {
            stampaVeramente(person);
        } catch (NonMiPiaceLId2Exception e) {
            System.out.println("A me invece l'id 2 piace");
        }
    }

    private static void stampaVeramente(Person person) throws NonMiPiaceLId3Exception {
        if (person.id == 3L)
            throw new NonMiPiaceLId3Exception();
        stampaVeramenteSulSerio (person);
    }

    private static void stampaVeramenteSulSerio(Person person) {
        if (person.id == 2L)
            throw new NonMiPiaceLId2Exception();
        String s = person.toString(true);
        System.out.printf (s);
    }

    static class NonMiPiaceLId2Exception extends RuntimeException {
    }

    static class NonMiPiaceLId3Exception extends Exception {
    }

}
