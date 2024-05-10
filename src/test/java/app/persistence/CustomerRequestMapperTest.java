package app.persistence;

import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerRequestMapperTest {

        private static final String USER = "postgres";
        private static final String PASSWORD = "HigAbt60ig";
        private static final String URL = "jdbc:postgresql://161.35.195.156/%s?currentSchema=public";
        private static final String DB = "carport_test";
        private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

        @BeforeEach
        void setup() {
            try (Connection testConnection = connectionPool.getConnection()) {
                try (Statement stmt = testConnection.createStatement()) {

                    stmt.execute("DELETE FROM customer_request");

                    // Reset the sequence number
                    stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', 1, false)");

                    // Insert rows
                    stmt.execute("INSERT INTO customer_request (length, height, width, date) VALUES " +
                            "(5, 3, 3, CURRENT_DATE)");
                    stmt.execute("INSERT INTO customer_request (length, height, width, date) VALUES " +
                            "(7, 3, 5, CURRENT_DATE)");

                    // Set sequence to continue from the largest member_id
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
    void getAllCustomerRequest() throws DatabaseException {
        LocalDate localDate = LocalDate.now();

        List<CustomerRequest> customerRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        assertEquals(2, customerRequests.size());


        assertEquals(5, customerRequests.get(0).getLength());
        assertEquals(3, customerRequests.get(0).getHeight());
        assertEquals(3, customerRequests.get(0).getWidth());
        assertEquals("Plasttrapezplader", customerRequests.get(0).getTileType());
        assertEquals("Afventer", customerRequests.get(0).getStatus());
        assertEquals(localDate, customerRequests.get(0).getDate());

        assertEquals(7, customerRequests.get(1).getLength());
        assertEquals(3, customerRequests.get(1).getHeight());
        assertEquals(5, customerRequests.get(1).getWidth());
        assertEquals("Plasttrapezplader", customerRequests.get(1).getTileType());
        assertEquals("Afventer", customerRequests.get(1).getStatus());
        assertEquals(localDate, customerRequests.get(1).getDate());
    }


    @Test
    void getCustomerRequestById() throws DatabaseException {
        List<CustomerRequest> allRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        int firstCustomerRequestId = allRequests.get(0).getCustomerRequestId();

        LocalDate localDate = LocalDate.now();
        CustomerRequest expected = new CustomerRequest(1, 5, 3, 3, localDate, "Afsluttet");
        CustomerRequest actual = new CustomerRequestMapper().getCustomerRequestById(firstCustomerRequestId, connectionPool);

        assertEquals(expected.getLength(), actual.getLength());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getTileType(), actual.getTileType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getStatus(), actual.getStatus());
    }


    @Test
    void deleteCustomerRequest() throws DatabaseException {
        CustomerRequestMapper.deleteCustomerRequest(2, connectionPool);
        assertEquals(1, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }

    @Test
    void makeCustomerRequest() throws DatabaseException {
        LocalDate localDate = LocalDate.now();
        CustomerRequestMapper.makeCustomerRequest(8, 3, 3, localDate, "Afventer", connectionPool);
        assertEquals(3, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }

    @Test
    void updateCustomerRequest() throws DatabaseException {
        LocalDate localDate = LocalDate.now();
        CustomerRequestMapper.updateCustomerRequest(1, 4, 3, localDate, "Klar", connectionPool);
        List<CustomerRequest> customerRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);

        assertEquals(4, customerRequests.get(0).getLength());
        assertEquals(3, customerRequests.get(0).getHeight());
        assertEquals(3, customerRequests.get(0).getWidth());
        assertEquals(localDate, customerRequests.get(0).getDate());
        assertEquals("Klar", customerRequests.get(0).getStatus());
    }
}