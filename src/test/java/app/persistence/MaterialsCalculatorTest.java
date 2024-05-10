package app.persistence;

import app.services.MaterialsCalculator;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MaterialsCalculatorTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport_v2";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection());
    }

    @Test
    void testCalculateAmountOfPosts(){
        MaterialsCalculator materialsCalculator = new MaterialsCalculator(600, 780, 210, connectionPool);
        int expectedQuantity = 6;

        int actualQuantity = materialsCalculator.calcPostQuantity();

        assertEquals(expectedQuantity, actualQuantity);
    }
}
