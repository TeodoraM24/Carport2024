package app;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.customer.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                stmt.execute("DELETE FROM customer_invoice");
                stmt.execute("DELETE FROM admin_customer_request");
                stmt.execute("DELETE FROM admin_invoice");
                stmt.execute("DELETE FROM admin_offer");
                stmt.execute("DELETE FROM admin_parts_list");
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

                // Reset the sequence number for offer
                stmt.execute("SELECT setval('public.customer_customer_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.offer_offer_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.parts_list_parts_list_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.price_price_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.carport_carport_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.invoice_invoice_id_seq', 1, false)");

                //Add data to tables
                stmt.execute("INSERT INTO customer (customer_id, first_name, last_name, email, password, phonenumber, address, zip) VALUES " +
                        "(1, 'Jon', 'Andersen', 'jon@blabla.com', '1234', 12455, 'Campusvej', 2770)");
                stmt.execute("INSERT INTO customer_request (customer_request_id, length, width, height, date) VALUES " +
                        "(1, 300, 330, 210, '2024-08-05')");
                stmt.execute("INSERT INTO price (purchase_price, salesprice_with_tax, coverage_ratio) VALUES " +
                        "(100, 600, 60)");
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Rem', '45', '195', '480', 20)");
                stmt.execute("INSERT INTO parts_list_item (material_id, amount, instruction_description, unit, total_price) VALUES " +
                        "(1, 6, 'test', 'Stk.', 100)");
                stmt.execute("INSERT INTO parts_list (parts_list_id, price_id) VALUES (1,1)");
                stmt.execute("UPDATE customer SET customer_request_id = 1 WHERE customer_id = 1");
                stmt.execute("INSERT INTO offer (offer_id, tool_shed_size, cladding_desc, rafter_type_desc, support_beam_desc_size, roof_materials, date, parts_list_id, price_id, customer_request_id) VALUES " +
                        "(1, '20', 'fkfk', 'eddd', 'Campusvdaej', 'blabla', '2024-05-14', 1, 1, 1)");
                stmt.execute("UPDATE customer SET offer_id = 1");
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
        int zip = 2770;
        int phoneNumber = 123456789;

        try {
            int customerId = CustomerMapper.createUser(email, password, firstName, lastName, zip, address, phoneNumber, connectionPool);
            assertEquals(2, customerId);
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testLogInValidCredentials() {
        String email = "jon@blabla.com";
        String password = "1234";

        try {
            Customer actualCustomer = CustomerMapper.login(email, password, connectionPool);

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
        assertThrows(DatabaseException.class, () -> CustomerMapper.login("invalid@gmail.com", "password", connectionPool));
    }

    @Test
    void testGetCustomerIdByOfferId() throws DatabaseException {
        int expectedCustomerId = 1;

        int actualCustomerId = CustomerMapper.getCustomerIdByOfferId(1, connectionPool);

        assertEquals(expectedCustomerId, actualCustomerId);
    }

    @Test
    void testRemoveOfferId() throws DatabaseException {
        CustomerMapper.removeOfferId(1, connectionPool);

        assertThrows(DatabaseException.class, () -> CustomerMapper.getCustomerIdByOfferId(1, connectionPool));
    }

    @Test
    void testRemoveRequestId() throws DatabaseException {
        int expectedRequestId = 0;

        CustomerMapper.removeRequestId(1, connectionPool);

        Customer customer = CustomerMapper.login("jon@blabla.com", "1234", connectionPool);
        int actualRequestId = customer.getCustomer_request_id();

        assertEquals(expectedRequestId, actualRequestId);
    }

}