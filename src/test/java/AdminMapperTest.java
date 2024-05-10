import app.entities.Admin;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminMapperTest {
    private static final String USER = "postgres";
    private static final String PASSWORD = "HigAbt60ig";
    private static final String URL = "jdbc:postgresql://161.35.195.156/%s?currentSchema=public";
    private static final String DB = "carport_test";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
        } catch (SQLException throwables) {
            fail("Database connection failed");
        }
    }

    @Test
    void testLogInValidCredentials() {
        String email = "admin@admin.com";
        String password = "admin";

        try {
            Admin actualAdmin = AdminMapper.logInd(email, password, connectionPool);

            assertNotNull(actualAdmin, "Admin doesn't exist(null)");

            assertEquals(email, actualAdmin.getEmail());
            assertEquals(password, actualAdmin.getPassword());
            assertEquals("admin", actualAdmin.getRole());
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
