package it.intesys.codylab.rookie.commandline;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ServizioPersone {
    final DataSource datasource;

    public ServizioPersone(DataSource datasource) {
        this.datasource = datasource;
    }

    public String process(List<Person> persons) throws SQLException {
        String outcome = randomOutcome();
        System.out.printf("Risultato: %s\n", outcome);
        if (outcome.equalsIgnoreCase("OK")) {
            for (Person person : persons) {
                process(person);
            }

        }
        return outcome;
    }

    private void process(Person person) throws SQLException {
        try (Connection connection = datasource.getConnection()) {
            String sql = """
                            INSERT INTO person 
                                (id, name, surname, registration_date)
                            VALUES 
                                (?, ?, ?, ?)
                    """;
            try (PreparedStatement query = connection.prepareStatement(sql)) {
                query.setLong(1, person.id);
                query.setString(2, person.name);
                query.setString(3, person.surname);
                query.setTimestamp(4, Timestamp.from(person.registrationDate));

                int rowUpdated = query.executeUpdate();
                if (rowUpdated != 1)
                    System.err.printf("Error inserting person %s: %d rows updated\n", person.toString(true), rowUpdated);
            }
        }
    }


    private String randomOutcome() {
        int timeModuloDue = (int) System.currentTimeMillis() % 2;
        String outcome;
        switch (timeModuloDue) {
            case 0:
                outcome = "OK";
                break;
            default:
                outcome = "ERROR";
        }
        return outcome;
    }

}
