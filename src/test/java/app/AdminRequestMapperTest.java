package app;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.AdminRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AdminRequestMapperTest {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement() ) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM customer");
                stmt.execute("DELETE FROM customer_request");

                // Reset the sequence number
                stmt.execute("SELECT setval('public.customer_customer_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip) VALUES " +
                        "('Jon', 'Andersen', 'jon@blabla.com', '1234', 12455, 'Campusvej', 2770)");
                stmt.execute("INSERT INTO customer_request (customer_request_id, length, width, height, date) VALUES " +
                        "(1, 300, 330, 210, '2024-08-05')");
                stmt.execute("UPDATE customer SET customer_request_id = 1 WHERE email = 'jon@blabla.com'");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.customer_customer_id_seq', COALESCE((SELECT MAX(customer_id)+1 FROM public.customer), 1), false)");
                stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', COALESCE((SELECT MAX(customer_request_id)+1 FROM public.customer_request), 1), false)");
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
    void testGetChosenCustomerRequestId() throws DatabaseException {
        //Arrange
        Customer chosenCustomer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej", 2770, "customer");
        int chosenCustomerId = chosenCustomer.getCustomerId();
        int expectedCustomerRequestId = 1;

        //Act
        int actualCustomerRequestId = AdminRequestMapper.getCustomerRequestId(chosenCustomerId, connectionPool);

        //Assert
        assertEquals(expectedCustomerRequestId, actualCustomerRequestId);
    }

    @Test
    void testGetChosenCustomerRequest() throws DatabaseException {
        //Arrange
        Customer chosenCustomer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej", 2770, "customer");
        int chosenCustomerId = chosenCustomer.getCustomerId();

        LocalDate date = LocalDate.of(2024, 8, 5);
        CustomerRequest expectedCustomerRequest = new CustomerRequest(1, 300, 330, 210, "Plasttrapezplader", date, "Afventer");

        //Act
        int customerRequestId = AdminRequestMapper.getCustomerRequestId(chosenCustomerId, connectionPool);
        CustomerRequest actualCustomerRequest = AdminRequestMapper.getCustomerRequest(customerRequestId, connectionPool);

        //Assert
        assertEquals(expectedCustomerRequest.hashCode(),actualCustomerRequest.hashCode());
    }

    @Test
    void  testUpdateCustomerRequest() throws DatabaseException {
        //Act
        int customerRequestId = 1;
        int length = 500;
        int width = 400;
        int height = 250;
        CustomerRequest expectedCustomerRequest = new CustomerRequest(customerRequestId, length, width, height, "Plasttrapezplader", LocalDate.of(2024, 8, 5), "Afventer");

        //Arrange
        AdminRequestMapper.updateCustomerRequest(customerRequestId, length, width, height, connectionPool);
        CustomerRequest actualCustomerRequest = AdminRequestMapper.getCustomerRequest(customerRequestId, connectionPool);

        //Assert
        assertEquals(expectedCustomerRequest.hashCode(), actualCustomerRequest.hashCode());
    }

    @Test
    void testUpdateCustomerRequestStatus() throws DatabaseException {
        String expectedStatus = "Klar";
        int customerRequestId = 1;

        AdminRequestMapper.updateCustomerRequestStatus(customerRequestId, expectedStatus, connectionPool);
        CustomerRequest customerRequest = AdminRequestMapper.getCustomerRequest(customerRequestId, connectionPool);
        String actualStatus = customerRequest.getStatus();
        assertEquals(expectedStatus, actualStatus);
    }
}
