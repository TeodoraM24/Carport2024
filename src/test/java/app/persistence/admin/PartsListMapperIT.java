package app.persistence.admin;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.PartsListMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PartsListMapperIT {
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
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Rem', '45', '195', '480', 20)");
                stmt.execute("INSERT INTO parts_list_item (material_id, amount, instruction_description, unit, total_price) VALUES " +
                        "(1, 6, 'test', 'Stk.', 100)");
                stmt.execute("INSERT INTO price (purchase_price, salesprice_with_tax, coverage_ratio) VALUES " +
                        "(100, 600, 60)");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.material_material_id_seq', COALESCE((SELECT MAX(material_id)+1 FROM public.material), 1), false)");
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', COALESCE((SELECT MAX(parts_list_item_id)+1 FROM public.parts_list_item), 1), false)");
                stmt.execute("SELECT setval('public.price_price_id_seq', COALESCE((SELECT MAX(price_id)+1 FROM public.price), 1), false)");
            }
        } catch (SQLException throwables) {
            fail("Database connection failed");
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
    void testAddPartsList() throws DatabaseException {
        int expectedPartsListId = 1;

        int actualPartsListId = PartsListMapper.addPartsList(1, connectionPool);

        assertEquals(expectedPartsListId, actualPartsListId);
    }

    @Test
    void testAddPartsListItem() throws DatabaseException {
        int expectedPartsListId = 1;
        PartsListMapper.addPartsList(1, connectionPool);

        int actualPartsListId = PartsListMapper.addPartsListItem(1, 1, connectionPool);

        assertEquals(expectedPartsListId, actualPartsListId);
    }
}
