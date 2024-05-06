package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "HigAbt60ig";
    private static final String URL = "jdbc:postgresql://161.35.195.156/%s?currentSchema=public";
    private static final String DB = "carport_test";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement() ) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM customer");

                // Reset the sequence number
                stmt.execute("SELECT setval('public.customer_customer_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip) VALUES " +
                        "('Jon', 'Andersen', 'jon@blabla.com', '1234', 12455, 'Campusvej', 2770)");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.customer_customer_id_seq', COALESCE((SELECT MAX(customer_id)+1 FROM public.customer), 1), false)");
            }
        } catch (SQLException throwables) {
            fail("Database connection failed");
        }
    }

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection());
    }

    @Test
    void testSelectUserFromDatabase() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                //Arrange
                Customer expectedCustomer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej", 2770, "customer", false);

                //Act
                ResultSet rs = stmt.executeQuery("SELECT * FROM customer WHERE email = 'jon@blabla.com' AND password = '1234'");

                Customer actualCustomer = null;
                if (rs.next()) {
                    int id = rs.getInt("customer_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    int phonenumber = rs.getInt("phonenumber");
                    String address = rs.getString("address");
                    int zip = rs.getInt("zip");
                    String role = rs.getString("role");

                    actualCustomer = new Customer(id, email, password, phonenumber, firstName, lastName, address, zip, role, false);
                }
                //Assert
                assertEquals(expectedCustomer.toString(), actualCustomer.toString());
            }
        } catch (SQLException throwables) {
            fail("Database connection failed");
        }
    }

    @Test
    void testLogInValidCredentials() {
        Customer expectedCustomer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej ", 3400, "customer", false);

        try {
            Customer actualCustomer = CustomerMapper.logInd("jon@blabla.com", "1234", connectionPool);
            assertEquals(expectedCustomer, actualCustomer);
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testLogInInvalidCredentials() {

        assertThrows(DatabaseException.class, () -> CustomerMapper.logInd("invalid@gmail.com", "password", connectionPool));
    }

    @Test
    void testCreateUser() {

        String email = "jesper@blabla.com";
        String password = "123456";
        String firstName = "Fornavn";
        String lastName = "Efternavn";
        int zip = 54321;
        int phoneNumber = 123456789;


        try {
            CustomerMapper.createUser(email, password, firstName, lastName, zip, phoneNumber, connectionPool);

            assertTrue(true);
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
