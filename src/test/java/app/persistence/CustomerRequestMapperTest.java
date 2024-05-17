package app.persistence;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
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
    private static final String DB = "carport_test";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

   @BeforeEach
    void setup() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {

                stmt.execute("DELETE FROM customer_parts_list");
                stmt.execute("DELETE FROM admin_customer_request");
                stmt.execute("DELETE FROM customer_request");
                stmt.execute("DELETE FROM admin");
                stmt.execute("DELETE FROM customer");


                // Reset sequence number for customer_request_id (assuming it's an auto-incremented sequence)
                stmt.executeUpdate("ALTER SEQUENCE customer_request_customer_request_id_seq RESTART WITH 1");
                stmt.execute("SELECT setval('public.admin_admin_id_seq', 1, false)");

                // Delete all records from customer
                stmt.executeUpdate("DELETE FROM customer");

                // Reset sequence number for customer_id (assuming it's an auto-incremented sequence)
                stmt.executeUpdate("ALTER SEQUENCE customer_customer_id_seq RESTART WITH 1");

                // Insert rows
                stmt.execute("INSERT INTO admin(email, password) VALUES ('admin@admin.dk', 'admin')");
                stmt.execute("INSERT INTO customer_request (length, width, height, date) VALUES " +
                        "(7, 5, 3, CURRENT_DATE)");
                stmt.execute("INSERT INTO customer_request (length, width, height, date) VALUES " +
                        "(8, 4, 3, CURRENT_DATE)");
                stmt.execute("INSERT INTO admin_customer_request (admin_id, customer_request_id) VALUES (1,2)");

                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip, customer_request_id) VALUES " +
                        "('Morten', 'Hvor blev du af', 'email@email.dk', '1234', 12345678, 'Vejen 2', 2770, 1)");

                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip, customer_request_id) VALUES " +
                        "('Lars', 'Larsen', 'email@email.live.dk', '1234', 12345679, 'Vejen 3', 2770, 2)");


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
    }

    @Test
    void getCustomerRequestById() throws DatabaseException {
        List<CustomerRequest> allRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        int firstCustomerRequestId = allRequests.get(0).getCustomerRequestId();

        LocalDate localDate = LocalDate.now();
        CustomerRequest expected = new CustomerRequest(1, 7, 5, 3, "Plasttrapezplader",localDate, "Afventer");
        CustomerRequest actual = new CustomerRequestMapper().getCustomerRequestById(firstCustomerRequestId, connectionPool);

        assertEquals(expected.getRequestLength(), actual.getRequestLength());
        assertEquals(expected.getRequestHeight(), actual.getRequestHeight());
        assertEquals(expected.getRequestWidth(), actual.getRequestWidth());
        assertEquals(expected.getRequestTileType(), actual.getRequestTileType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

   @Test
    void deleteCustomerRequest() throws DatabaseException {
        CustomerRequestMapper.deleteCustomerRequest(1, connectionPool);
        assertEquals(1, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }

    @Test
    void makeCustomerRequest() throws DatabaseException {
        Customer currentCustomer = new Customer(0, "test@email.live.dk", "123", 87654321, "Johny", "Deluxe", "test", 2770,"customer");
 
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement insertCustomerStatement = connection.prepareStatement("INSERT INTO customer (email, password, phonenumber, first_name, last_name, address, zip) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            insertCustomerStatement.setString(1, currentCustomer.getEmail());
            insertCustomerStatement.setString(2, currentCustomer.getPassword());
            insertCustomerStatement.setInt(3, currentCustomer.getPhoneNumber());
            insertCustomerStatement.setString(4, currentCustomer.getFirstName());
            insertCustomerStatement.setString(5, currentCustomer.getLastName());
            insertCustomerStatement.setString(6, currentCustomer.getAddress());
            insertCustomerStatement.setInt(7, currentCustomer.getZip());

            int rowsAffected = insertCustomerStatement.executeUpdate();

            if (rowsAffected != 1) {
                fail("Failed to insert customer");
            }

            ResultSet generatedKeys = insertCustomerStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int customerId = generatedKeys.getInt(1);

                LocalDate date = LocalDate.now();
                int height = 300;
                int width = 400;
                int length = 600;

                int initialSize = new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size();
                CustomerRequestMapper.makeCustomerRequest(currentCustomer, height, width, length, date, connectionPool);
                int updatedSize = new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size();

                assertEquals(initialSize + 1, updatedSize);
            } else {
                fail("Failed to retrieve generated key for customer");
            }
        } catch (SQLException e) {
            fail("Database error: " + e.getMessage());
        }
    }





    @Test
    void updateCustomerRequest() throws DatabaseException {
        List<CustomerRequest> customerRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        int firstCustomerRequestId = customerRequests.get(0).getCustomerRequestId(); // Assuming the first one

        LocalDate localDate = LocalDate.now();
        CustomerRequestMapper.updateCustomerRequest(firstCustomerRequestId, 9, 9, 9, localDate, "Klar", connectionPool);

        CustomerRequest updatedRequest = new CustomerRequestMapper().getCustomerRequestById(firstCustomerRequestId, connectionPool);

        assertEquals(9, updatedRequest.getRequestLength());
        assertEquals(9, updatedRequest.getRequestHeight());
        assertEquals(9, updatedRequest.getRequestWidth());
        assertEquals(localDate, updatedRequest.getDate());
        assertEquals("Klar", updatedRequest.getStatus());
    }

    @Test
    void testGetAllCustomerRequests() {
        int customerId = 1;
        try {
            List<CustomerRequest> customerRequests = CustomerRequestMapper.getAllCustomerRequests(customerId, connectionPool);
            assertNotNull(customerRequests);
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}
