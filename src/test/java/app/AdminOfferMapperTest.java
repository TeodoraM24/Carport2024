package app;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.AdminOfferMapper;
import app.persistence.admin.PartsListMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AdminOfferMapperTest {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Remove all rows from relevant tables
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

                // Reset the sequence number
                stmt.execute("SELECT setval('public.offer_offer_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.parts_list_parts_list_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.price_price_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.customer_request_customer_request_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO customer_request (length, width, height, tile_type, date, status) VALUES " +
                        "(600, 400, 300, 'Plasttrapezplader', '2024-08-14', 'Klar')");
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Rem', '45', '195', '480', 20)");
                stmt.execute("INSERT INTO parts_list_item (material_id, amount, instruction_description, unit, total_price) VALUES " +
                        "(1, 6, 'test', 'Stk.', 100)");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.offer_offer_id_seq', COALESCE((SELECT MAX(offer_id)+1 FROM public.offer), 1), false)");
                stmt.execute("SELECT setval('public.parts_list_parts_list_id_seq', COALESCE((SELECT MAX(parts_list_id)+1 FROM public.parts_list), 1), false)");
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', COALESCE((SELECT MAX(parts_list_item_id)+1 FROM public.parts_list_item), 1), false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', COALESCE((SELECT MAX(material_id)+1 FROM public.material), 1), false)");
                stmt.execute("SELECT setval('public.price_price_id_seq', COALESCE((SELECT MAX(price_id)+1 FROM public.price), 1), false)");
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
    void testAddPrice() throws DatabaseException {
        int expectedPriceId = 1;

        int actualPriceId = AdminOfferMapper.addPrice(100,300,30,connectionPool);

        assertEquals(expectedPriceId, actualPriceId);
    }

    @Test
    void testAddOffer() throws DatabaseException {
        int expectedRowsAffected = 1;
        LocalDate date = LocalDate.of(2024,5,14);
        int priceId = AdminOfferMapper.addPrice(100,300,30,connectionPool);
        int partsListId = PartsListMapper.addPartsList(priceId, connectionPool);

        int actualRowsAffected = AdminOfferMapper.addOffer("test","test","test", date, partsListId, priceId, 1, connectionPool);
        assertEquals(expectedRowsAffected, actualRowsAffected);
    }
}
