package app;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.PartsListItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PartsListItemMapperTest {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM parts_list_parts_list_item");
                stmt.execute("DELETE FROM parts_list_item");
                stmt.execute("DELETE FROM material");

                // Reset the sequence number
                stmt.execute("SELECT setval('public.parts_list_item_parts_list_item_id_seq', 1, false)");
                stmt.execute("SELECT setval('public.material_material_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Rem', '45', '195', '480', 20)");

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
        int expectedPartsListId = 1;
        int actualPartsListId = PartsListItemMapper.addPartsListItem(1, 2, "test", "Stk.",100, connectionPool);

        assertEquals(expectedPartsListId, actualPartsListId);
    }

}
