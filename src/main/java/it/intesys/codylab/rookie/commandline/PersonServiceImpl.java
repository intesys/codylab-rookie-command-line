package it.intesys.codylab.rookie.commandline;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Servizio
public class PersonServiceImpl implements PersonService {
    DataSource datasource;

    public PersonServiceImpl() {
    }

    public PersonServiceImpl(DataSource datasource) {
        this.datasource = datasource;
    }

    public void process(List<Person> persons) throws IOException {
        for (Person person : persons) {
            try {
                process(person);
            } catch (SQLException e) {
                throw new IOException(e);
            }
        }
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
}
