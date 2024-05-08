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
                //stmt.execute("DELETE FROM customer");
                // Reset the sequence number for customer_id
                stmt.execute("SELECT setval('public.customer_customer_id_seq', 1, false)");
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

    @Test
    void testLogInValidCredentials() {
        String email = "jesper@blabla.com";
        String password = "123456";

        try {
            Customer actualCustomer = CustomerMapper.logInd(email, password, connectionPool);

            assertNotNull(actualCustomer, "Customer doesn't exist(null)");

            assertEquals(email, actualCustomer.getEmail());
            assertEquals(password, actualCustomer.getPassword());
            assertEquals("Fornavn", actualCustomer.getFirstName());
            assertEquals("Efternavn", actualCustomer.getLastName());
            assertEquals("Testvej", actualCustomer.getAddress());
            assertEquals(3400, actualCustomer.getZip());
            assertEquals(123456789, actualCustomer.getPhoneNumber());
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testLogInInvalidCredentials() {
        assertThrows(DatabaseException.class, () -> CustomerMapper.logInd("invalid@gmail.com", "password", connectionPool));
    }

}


