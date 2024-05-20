package app;

import app.entities.Invoice;
import app.entities.InvoiceDetails;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.customer.InvoiceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceMapperTest {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM public.admin_customer_request");
                stmt.execute("DELETE FROM public.customer_invoice");
                stmt.execute("DELETE FROM public.invoice");
                stmt.execute("DELETE FROM public.parts_list_parts_list_item");
                stmt.execute("DELETE FROM public.parts_list_item");
                stmt.execute("DELETE FROM public.parts_list");
                stmt.execute("DELETE FROM public.carport");
                stmt.execute("DELETE FROM public.price");
                stmt.execute("DELETE FROM public.material");
                stmt.execute("DELETE FROM public.customer");
                stmt.execute("DELETE FROM public.customer_request");


                // Insert rows
                stmt.execute("INSERT INTO public.customer_request( customer_request_id, length, width, height, tile_type, date, status) VALUES (2, 34, 45, 98, 'Trapez', '2025-03-07', 'Afsluttet');");
                stmt.execute("INSERT INTO public.customer( customer_id, first_name, last_name, email, password, phonenumber, address, zip, role, customer_request_id) VALUES (2, 'Hans', 'Hansen', 'hans@gmail.com', '1234', 98562458, 'Østerbro', 2100 , 'customer', 2);");
                stmt.execute("INSERT INTO public.price( price_id, purchase_price, salesprice_with_tax, coverage_ratio) VALUES (2, 432.32, 485.78, 9854.48);");
                stmt.execute("INSERT INTO public.carport( carport_id, width, height, length, price_id) VALUES (2, 48, 79, 65, 2);");
                stmt.execute("INSERT INTO public.parts_list( parts_list_id, price_id) VALUES (2, 2);");
                stmt.execute("INSERT INTO public.invoice( invoice_id, parts_list_id, date, carport_id) VALUES (2, 2, '2025-06-14', 2);");
                stmt.execute("INSERT INTO public.material( material_id, material_description, height, width, length, price) VALUES (2, 'HJÆLPE TEKST', 32, 54, 78, 258.89);");
                stmt.execute("INSERT INTO public.parts_list_item( parts_list_item_id, material_id, amount, instruction_description, unit, total_price) VALUES (2, 2, 23,'TESTER', 'roller', 232.78);");
                stmt.execute("INSERT INTO public.parts_list_parts_list_item( parts_list_id, parts_list_item_id) VALUES (2, 2);");
                stmt.execute("INSERT INTO public.customer_invoice( customer_id, invoice_id) VALUES (2, 2);");


                // Set sequence to continue from the largest invoice_id
                stmt.execute("SELECT setval('public.invoice_invoice_id_seq', COALESCE((SELECT MAX(invoice_id)+1 FROM public.invoice), 1), false)");
            }
        } catch (SQLException throwables) {
            fail("Database setup failed: " + throwables.getMessage());
        }
    }

    @Test
    void testConnection() {
        try {
            assertNotNull(connectionPool.getConnection(), "Connection pool should initialize with a valid connection.");
        } catch (SQLException e) {
            fail("Failed to obtain a connection from the pool: " + e.getMessage());
        }
    }

    @Test
    void getOrderHistory () throws DatabaseException{
        List<Invoice> invoices = InvoiceMapper.getCustomersOrderHistory(2,connectionPool);
        assertEquals(1,invoices.size());
    }

    @Test
    void OrderHistoryWithNoInvoices () throws DatabaseException {
        List<Invoice> invoices = InvoiceMapper.getCustomersOrderHistory(3, connectionPool);
        assertTrue(invoices.isEmpty());
    }

    @Test
    void getInvoiceDetails () throws DatabaseException {
        List<InvoiceDetails> invoiceDetails = InvoiceMapper.getCustomerInvoiceDetails(2,2,connectionPool);
        assertEquals(1,invoiceDetails.size());
    }

    @Test
    void getInvoiceWithNoDetails () throws DatabaseException {
        List<InvoiceDetails> invoiceDetails = InvoiceMapper.getCustomerInvoiceDetails(3, 2, connectionPool);
        assertTrue(invoiceDetails.isEmpty());
    }
}