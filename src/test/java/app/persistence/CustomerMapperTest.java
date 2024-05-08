package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
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
            try (Statement stmt = testConnection.createStatement()) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM customer");
                // Reset the sequence number for customer_id
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
    void testCreateUser() {
        String email = "jesper@blabla.com";
        String password = "123456";
        String firstName = "Fornavn";
        String lastName = "Efternavn";
        String address = "Testvej";
        int zip = 3400;
        int phoneNumber = 123456789;

        try {
            CustomerMapper.createUser(email, password, firstName, lastName, zip, address, phoneNumber, connectionPool);
            assertTrue(true);
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    //'Jon', 'Andersen', 'jon@blabla.com', '1234', 12455, 'Campusvej', 2770
    @Test
    void testLogInValidCredentials() {
        String email = "jon@blabla.com";
        String password = "1234";

        try {
            Customer actualCustomer = CustomerMapper.logInd(email, password, connectionPool);

            assertNotNull(actualCustomer, "Customer doesn't exist(null)");

            assertEquals(email, actualCustomer.getEmail());
            assertEquals(password, actualCustomer.getPassword());
            assertEquals("Jon", actualCustomer.getFirstName());
            assertEquals("Andersen", actualCustomer.getLastName());
            assertEquals("Campusvej", actualCustomer.getAddress());
            assertEquals(2770, actualCustomer.getZip());
            assertEquals(12455, actualCustomer.getPhoneNumber());
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testLogInInvalidCredentials() {
        assertThrows(DatabaseException.class, () -> CustomerMapper.logInd("invalid@gmail.com", "password", connectionPool));
    }

}