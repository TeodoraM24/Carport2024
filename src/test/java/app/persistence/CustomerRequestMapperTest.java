package app.persistence;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;
import org.checkerframework.checker.units.qual.C;
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
/*
    private static final String USER = "postgres";
    private static final String PASSWORD = "HigAbt60ig";
    private static final String URL = "jdbc:postgresql://161.35.195.156/%s?currentSchema=public";
    private static final String DB = "carport_test";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);*/

    private static final String USER ="postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport_test_local";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

   @BeforeEach
    void setup() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {

                stmt.executeUpdate("DELETE FROM customer_request");

                // Reset sequence number for customer_request_id (assuming it's an auto-incremented sequence)
                stmt.executeUpdate("ALTER SEQUENCE customer_request_customer_request_id_seq RESTART WITH 1");

                // Delete all records from customer
                stmt.executeUpdate("DELETE FROM customer");

                // Reset sequence number for customer_id (assuming it's an auto-incremented sequence)
                stmt.executeUpdate("ALTER SEQUENCE customer_customer_id_seq RESTART WITH 1");

                // Insert rows
                stmt.execute("INSERT INTO customer_request (length, width, height, date) VALUES " +
                        "(7, 5, 3, CURRENT_DATE)");
                stmt.execute("INSERT INTO customer_request (length, width, height, date) VALUES " +
                        "(8, 4, 3, CURRENT_DATE)");

                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip) VALUES " +
                        "('Morten', 'Hvor blev du af', 'email@email.dk', '1234', 12345678, 'Vejen 2', 2770)");

                // Set sequence to continue from the largest member_id
                //stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', COALESCE((SELECT MAX(customer_request_id)+1 FROM public.customer_request), 1), false)");
                //stmt.execute("SELECT setval('public.customer_customer_id_seq', COALESCE((SELECT MAX(customer_id)+1 FROM public.customer), 1), false))))");
            }
        } catch (SQLException throwables) {
            fail("Database connection failed"+throwables.getMessage());

        }
    }

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection());
    }

    @Test
    void getAllCustomerRequest() throws DatabaseException {

        List<CustomerRequest> customerRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);

        for (CustomerRequest customerRequest : customerRequests) {
            System.out.println(customerRequest.toString());
            System.out.println("--------------------------------");
        }
        assertEquals(2, customerRequests.size());

/*
        assertEquals(7, customerRequests.get(0).getLength());
        assertEquals(5, customerRequests.get(0).getWidth());
        assertEquals(3, customerRequests.get(0).getHeight());
        assertEquals("Plasttrapezplader", customerRequests.get(0).getTileType());
        assertEquals("Afventer", customerRequests.get(0).getStatus());
        assertEquals(localDate, customerRequests.get(0).getDate());

        assertEquals(8, customerRequests.get(1).getLength());
        assertEquals(4, customerRequests.get(1).getWidth());
        assertEquals(3, customerRequests.get(1).getHeight());
        assertEquals("Plasttrapezplader", customerRequests.get(1).getTileType());
        assertEquals("Afventer", customerRequests.get(1).getStatus());
        assertEquals(localDate, customerRequests.get(1).getDate());*/
    }


    @Test
    void getCustomerRequestById() throws DatabaseException {
        List<CustomerRequest> allRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        int firstCustomerRequestId = allRequests.get(0).getCustomerRequestId();

        LocalDate localDate = LocalDate.now();
        CustomerRequest expected = new CustomerRequest(1, 7, 5, 3, "Plasttrapezplader",localDate, "Afventer");
        CustomerRequest actual = new CustomerRequestMapper().getCustomerRequestById(firstCustomerRequestId, connectionPool);

        assertEquals(expected.getLength(), actual.getLength());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getTileType(), actual.getTileType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getStatus(), actual.getStatus());
    }


   /* @Test
    void deleteCustomerRequest() throws DatabaseException {
        CustomerRequestMapper.deleteCustomerRequest(1, connectionPool);
        assertEquals(0, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }
/*
    @Test
    void makeCustomerRequest() throws DatabaseException {
        // 1. Prepare Data
        LocalDate localDate = LocalDate.now();
        int initialSize = new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size();

        // 2. Invoke the Method
        CustomerRequestMapper.makeCustomerRequest(connectionPool);

        // 3. Assertion
        int updatedSize = new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size();
        assertEquals(initialSize + 1, updatedSize);
    }*/


    @Test
    void updateCustomerRequest() throws DatabaseException {
        List<CustomerRequest> customerRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        int firstCustomerRequestId = customerRequests.get(0).getCustomerRequestId(); // Assuming the first one

        LocalDate localDate = LocalDate.now();
        CustomerRequestMapper.updateCustomerRequest(firstCustomerRequestId, 9, 9, 9, localDate, "Klar", connectionPool);

        CustomerRequest updatedRequest = new CustomerRequestMapper().getCustomerRequestById(firstCustomerRequestId, connectionPool);

        assertEquals(9, updatedRequest.getLength());
        assertEquals(9, updatedRequest.getHeight());
        assertEquals(9, updatedRequest.getWidth());
        assertEquals(localDate, updatedRequest.getDate());
        assertEquals("Klar", updatedRequest.getStatus());
    }

}