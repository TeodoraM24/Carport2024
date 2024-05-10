package app.persistence;

import app.entities.PartsListItem;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PartsListItemMapperTest {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport_v2";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM parts_list_item");
                stmt.execute("DELETE FROM material");

                // Reset the sequence number
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Rem', '45', '195', '480', 220)");
                stmt.execute("INSERT INTO parts_list_item (material_id, amount, instruction_description, unit_id) VALUES " +
                        "(1, 6, 'test', 1)");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', COALESCE((SELECT MAX(parts_list_item_id)+1 FROM public.parts_list_item), 1), false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', COALESCE((SELECT MAX(material_id)+1 FROM public.material), 1), false)");
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
    void testAddPartsListItem() throws DatabaseException {
        int expectedSize = 2;
        PartsListItemMapper.addPartsListItem(1, 2, "test", 1, connectionPool);

        List<PartsListItem> partsListItems = PartsListItemMapper.getAllPartsListItems(connectionPool);

        assertEquals(expectedSize, partsListItems.size());
    }

    @Test
    void testGetPartsListItemIdByData() throws DatabaseException {
        int expectedId = 1;
        int materialId = 1;
        int amount = 6;
        String instruction = "test";
        int unitId = 1;

        int actualPartsListItemId = PartsListItemMapper.getPartsListItemIdByData(materialId, amount, instruction, unitId, connectionPool);

        assertEquals(expectedId, actualPartsListItemId);
    }
}
