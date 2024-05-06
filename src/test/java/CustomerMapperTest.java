import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    @BeforeEach
    void setUp() {

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
