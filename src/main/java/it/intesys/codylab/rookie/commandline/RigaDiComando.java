package it.intesys.codylab.rookie.commandline;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;

public class RigaDiComando {
    public static final String INSTANT_PATTERN = "d MMMM uuuu HH:mm";
    public static final Locale INSTANT_LOCALE = Locale.ITALIAN;
    public static final String INSTANT_TIME_ZONE = "Europe/Rome";

    static void main(String[] arguments)  {
        //interpretazione dell'intput
        ArrayList<Person> persons = readInput(arguments);

        //elaborazione
        boolean process = process(persons);

        //generazione dell'output
        System.out.printf("OK: %b", process);
    }

    private static boolean process(ArrayList<Person> persons) {
        for (Person person : persons) {
            System.out.println(person.toString(true));
        }
        return true;
    }


    private static ArrayList<Person> readInput(String[] arguments) {
        ArrayList<Person> persons = new ArrayList<Person>();

        for (int i = 0; i < arguments.length; i++){
            switch (arguments[i]){
                case "--person":
                    Person person = new Person();
                    PERSON: for (i++; i < arguments.length; i++) {
                        switch (arguments[i]) {
                            case "-name":
                                person.name = arguments[++i];
                                break;
                            case "-surname":
                                person.surname = arguments[++i];
                                break;
                            case "-registrationDate":
                                String registrationDateString = arguments[++i];
                                person.registrationDate = parseInstant (registrationDateString);
                                break;
                            default:
                                i--;
                                break PERSON;
                        }
                    }
                    persons.add(person);
                    break;
                default:
                    error(arguments[i]);
            }
        }

        return persons;
    }

    private static void error(String arguments) {
        System.err.printf("Unknown argument: %s%n", arguments);
        System.exit(-1);
    }

    private static Instant parseInstant (String s) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive() // accetta "Giugno" o "giugno"
                .appendPattern(INSTANT_PATTERN)
                .toFormatter(INSTANT_LOCALE);

        LocalDateTime ldt = LocalDateTime.parse(s, formatter);

        // Scegli il fuso corretto del tuo contesto applicativo
        return ldt.atZone(ZoneId.of(INSTANT_TIME_ZONE)).toInstant();
    }
}
