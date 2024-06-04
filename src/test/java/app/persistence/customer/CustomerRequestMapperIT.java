package app.persistence.customer;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.customer.CustomerRequestMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerRequestMapperIT {
    private static ConnectionPool connectionPool;

    @BeforeAll
    static void setUpDB() {
        connectionPool = ConnectionPool.getInstance();
    }

    @BeforeEach
     void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                stmt.execute("DELETE FROM customer_invoice");
                stmt.execute("DELETE FROM customer");
                stmt.execute("DELETE FROM invoice");
                stmt.execute("DELETE FROM offer");
                stmt.execute("DELETE FROM parts_list_parts_list_item");
                stmt.execute("DELETE FROM parts_list");
                stmt.execute("DELETE FROM parts_list_item");
                stmt.execute("DELETE FROM material");
                stmt.execute("DELETE FROM carport");
                stmt.execute("DELETE FROM price");
                stmt.execute("DELETE FROM customer_request");

                stmt.execute("SELECT setval('public.customer_customer_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.invoice_invoice_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.offer_offer_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.parts_list_parts_list_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.carport_carport_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.price_price_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO customer_request (length, width, height, date) VALUES " +
                        "(7, 5, 3, CURRENT_DATE)");
                stmt.execute("INSERT INTO customer_request (length, width, height, date) VALUES " +
                        "(8, 4, 3, CURRENT_DATE)");
                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip, customer_request_id) VALUES " +
                        "('Morten', 'Hvor blev du af', 'email@email.dk', '1234', 12345678, 'Vejen 2', 2770, 1)");

                stmt.execute("INSERT INTO customer (first_name, last_name, email, password, phonenumber, address, zip) VALUES " +
                        "('Lars', 'Larsen', 'email@email.live.dk', '1234', 12345679, 'Vejen 3', 2770)");

                stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', COALESCE((SELECT MAX(customer_request_id)+1 FROM public.customer_request), 1), false)");
                stmt.execute("SELECT setval('public.customer_customer_id_seq', COALESCE((SELECT MAX(customer_id)+1 FROM public.customer), 1), false)");
            }
        } catch (SQLException throwables) {
            fail("Database connection failed" + throwables.getMessage());

        }
    }

    @AfterAll
    static void tearDown() {
        if (connectionPool != null) {
            connectionPool.close();
            ConnectionPool.instance = null;
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
        assertEquals(1, customerRequests.size());
    }

    @Test
    void getCustomerRequestById() throws DatabaseException {
        List<CustomerRequest> allRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        int firstCustomerRequestId = allRequests.get(0).getCustomerRequestId();

        LocalDate localDate = LocalDate.now();
        CustomerRequest expected = new CustomerRequest(1, 7, 5, 3, "Plasttrapezplader", localDate, "Afventer");
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
        assertEquals(0, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }

    @Test
    void makeCustomerRequest() throws DatabaseException {
        Customer currentCustomer = new Customer(2, "email@email.live.dk", "1234", 12345679, "Lars", "Larsen", "Vejen 3", 2770, "customer");
        LocalDate date = LocalDate.now();
        int height = 300;
        int width = 400;
        int length = 600;
        int expectedCustomerRequestId = 3;

        int customerRequestId = CustomerRequestMapper.makeCustomerRequest(currentCustomer, height, width, length, date, connectionPool);

        assertEquals(expectedCustomerRequestId, customerRequestId);
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
