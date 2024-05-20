package app;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.PurchaseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseMapperTest {
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
                stmt.execute("INSERT INTO carport (width, height, length, price_id) VALUES (400, 300, 600, 1)");
                stmt.execute("INSERT INTO invoice (parts_list_id, date, carport_id) VALUES (1, '2024-08-05', 1)");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.customer_customer_id_seq', COALESCE((SELECT MAX(customer_id)+1 FROM public.customer), 1), false)");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            fail("Database connection failed");
        }
    }

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection());
    }

    @Test
    void testAddCarport() throws DatabaseException {
        int expectedCarportId = 2;

        int actualCarportId = PurchaseMapper.addCarport(400, 300, 500, 1, connectionPool);

        assertEquals(expectedCarportId, actualCarportId);
    }

    @Test
    void testAddInvoice() throws DatabaseException {
        int expectedInvoiceId = 2;
        LocalDate date = LocalDate.now();

        int actualInvoiceId = PurchaseMapper.addInvoice(1, date, 1, connectionPool);

        assertEquals(expectedInvoiceId, actualInvoiceId);
    }

    @Test
    void testAddInvoiceToCustomer() throws DatabaseException {
        int expectedCustomerId = 1;

        int actualCustomerId = PurchaseMapper.addInvoiceToCustomer(1, 1, connectionPool);

        assertEquals(expectedCustomerId, actualCustomerId);
    }
}
