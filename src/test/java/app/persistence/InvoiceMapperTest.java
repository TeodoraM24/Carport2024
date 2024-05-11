package app.persistence;

import app.entities.Invoice;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceMapperTest {

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
                stmt.execute("DELETE FROM customer_invoice");
                stmt.execute("DELETE FROM customer_carport");
                stmt.execute("DELETE FROM carport_parts_list");
                stmt.execute("DELETE FROM parts_list");
                stmt.execute("DELETE FROM invoice");

                // Insert rows

                //Make a invoice --> Insert rows
                stmt.execute("INSERT INTO public.customer_request( customer_request_id, length, width, height, tile_type, date, status) VALUES (4, 45, 98, 78, 'TEST', '2025-06-08', 'Afsluttet')");
                stmt.execute("INSERT INTO public.customer( customer_id, first_name, last_name, email, password, phonenumber, address, zip, role, customer_request_id, offer_id) VALUES (5, 'Rebecca', 'Sørensen', 'rebecca@gmail.com', 'hej321', 54852674, 'Østerbro', 2100, 'customer', 4, 3);");
                stmt.execute("INSERT INTO public.customer_invoice( customer_id, invoice_id) VALUES (5,6)");
                stmt.execute("INSERT INTO public.customer_carport( customer_id, carport_id) VALUES (5, 7)");
                stmt.execute("INSERT INTO public.carport_parts_list( carport_id, parts_list_id) VALUES (7, 8 )");
                stmt.execute("INSERT INTO public.parts_list( parts_list_id, amount, unit_id, instruction_description, price_id) VALUES (8, 9,7 , 'TEST', 200)");
                stmt.execute("INSERT INTO invoice (parts_list_id, date, carport_id) VALUES (8, '2024-05-03',7 )");

                // Set sequence to continue from the largest invoice_id
                stmt.execute("SELECT setval('public.invoice_invoice_id_seq', COALESCE((SELECT MAX(invoice_id)+1 FROM public.invoice), 1), false)");
            }
        } catch (SQLException throwables) {
            fail("Database connection failed");
        }
    }
@Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection());
    }

  /*
    @Test
  void getACustomersInvoice() throws DatabaseException {
        List<Invoice> invoices = InvoiceMapper.getACustomersInvoice(1, connectionPool);
        assertEquals(1, invoices.size());
    }*/
}
