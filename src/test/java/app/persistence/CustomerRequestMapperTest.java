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

                    stmt.execute("DELETE FROM customer_requests");

                    // Reset the sequence number
                    stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', 1, false)");

                    // Insert rows
                    stmt.execute("INSERT INTO customer_request (length, height, width, tile_type, date, status) VALUES " +
                            "(5, 3, 3, 'plast trapez', CURRENT_DATE, 'Afventer')");
                    stmt.execute("INSERT INTO customer_request (length, height, width, tile_type, date, status) VALUES " +
                            "(7, 3, 5, 'plast trapez', CURRENT_DATE, 'Klar')");

                    // Set sequence to continue from the largest member_id
                    //stmt.execute("SELECT setval('public.material_material_id_seq', COALESCE((SELECT MAX(material_id)+1 FROM public.material), 1), false)");
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

        List<CustomerRequest> customerRequest = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        assertEquals(2, customerRequest.size());
        assertEquals(new CustomerRequest(1, 5, 3, 3, "plast trapez", "Afventer", localDate), customerRequest.get(0));
        assertEquals(new CustomerRequest(2, 7, 3, 5, "plast trapez", "Klar", localDate), customerRequest.get(1));
    }

    @Test
    void getCustomerRequestById() throws DatabaseException {
            List<CustomerRequest> allRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
            int firstCustomerRequestId = allRequests.get(0).getCustomerRequestId();

            CustomerRequest customerRequest = new CustomerRequestMapper().getCustomerRequestById(firstCustomerRequestId, connectionPool);
            assertEquals(allRequests.get(0), customerRequest);
    }

    @Test
    void deleteCustomerRequest() throws DatabaseException {
        CustomerRequestMapper.deleteCustomerRequest(2, connectionPool);
        assertEquals(1, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }

    @Test
    void makeCustomerRequest() throws DatabaseException {
        LocalDate localDate = LocalDate.now();
        CustomerRequestMapper.makeCustomerRequest(8, 3, 3, localDate, connectionPool);
        assertEquals(3, new CustomerRequestMapper().getAllCustomerRequest(connectionPool).size());
    }

    @Test
    void updateCustomerRequest() throws DatabaseException {
        LocalDate localDate = LocalDate.now();
        CustomerRequestMapper.updateCustomerRequest(1, 4, 2, 2, localDate, "Klar", connectionPool);
        List<CustomerRequest> customerRequests = new CustomerRequestMapper().getAllCustomerRequest(connectionPool);
        assertEquals(4, customerRequests.get(0).getLength());
        assertEquals(2, customerRequests.get(0).getHeight());
        assertEquals(2, customerRequests.get(0).getWidth());
        assertEquals(localDate, customerRequests.get(0).getDate());
        assertEquals("Klar", customerRequests.get(0).getStatus());
    }
}
