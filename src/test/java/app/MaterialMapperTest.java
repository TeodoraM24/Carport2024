package app;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.MaterialMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaterialMapperTest {
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
                stmt.execute("SELECT setval('public.material_material_id_seq', 1, false)");

                // Insert rows
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Rem', '45', '195', '480', 220)");
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Spærre', '45', '195', '600', 520)");
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Stolpe', '97', '97', '300', 170)");
                stmt.execute("INSERT INTO material (material_description, height, width, length, price) VALUES " +
                        "('Plasttrapez', '110', '410', '620', 160)");

                // Set sequence to continue from the largest member_id
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
    void getAllMaterials() throws DatabaseException {
        List<Material> materials = MaterialMapper.getAllMaterials(connectionPool);
        assertEquals(4, materials.size());
        assertEquals(new Material(1, "Rem", 45, 195, 480, 220), materials.get(0));
        assertEquals(new Material(2, "Spærre", 45, 195, 600, 520), materials.get(1));
        assertEquals(new Material(3, "Stolpe", 97, 97, 300, 170), materials.get(2));
        assertEquals(new Material(4, "Plasttrapez", 110, 410, 620, 160), materials.get(3));

    }

    @Test
    void deleteMaterial() throws DatabaseException {
        MaterialMapper.deleteMaterial(2, connectionPool);
        assertEquals(3, MaterialMapper.getAllMaterials(connectionPool).size());
    }

    @Test
    void addMaterial() throws DatabaseException {
        MaterialMapper.addMaterial("Bøgetræ", 10, 110, 240, 140, connectionPool);
        assertEquals(5, MaterialMapper.getAllMaterials(connectionPool).size());
    }

    @Test
    void updateMaterial() throws DatabaseException {
        MaterialMapper.updateMaterial(4, "Plasttrapez", 110, 700, 620, 240, connectionPool);
        List <Material> materials = MaterialMapper.getAllMaterials(connectionPool);
        assertEquals("Plasttrapez", materials.get(3).getDescription());
        assertEquals(110, materials.get(3).getHeight());
        assertEquals(700, materials.get(3).getWidth());
        assertEquals(620, materials.get(3).getLength());
        assertEquals(240, materials.get(3).getPrice());
    }

    @Test
    void testGetMaterialIdByDataInvalid() throws DatabaseException {
        int expectedId = -1;
        int height = 300;
        int totalPrice = (height / 100) * 35;
        int postLengthMM = 97;
        int postWidthMM = 97;
        String postDesc = postLengthMM + "x" + postWidthMM + " mm. trykimp. Stolpe";

        int actualMaterial = MaterialMapper.getMaterialIdByData(postDesc, height, postWidthMM, postLengthMM, totalPrice, connectionPool);

        assertEquals(expectedId, actualMaterial);
    }

    @Test
    void testGetMaterialIdByDataValid() throws DatabaseException {
        int expectedId = 5;
        int height = 300;
        int totalPrice = (height / 100) * 35;
        int postLengthMM = 97;
        int postWidthMM = 97;
        String postDesc = postLengthMM + "x" + postWidthMM + " mm. trykimp. Stolpe";

        MaterialMapper.addMaterial(postDesc, height, postWidthMM, postLengthMM, totalPrice, connectionPool);
        int actualMaterial = MaterialMapper.getMaterialIdByData(postDesc, height, postWidthMM, postLengthMM, totalPrice, connectionPool);
        assertEquals(expectedId, actualMaterial);
    }
}
