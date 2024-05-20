package app;

import app.entities.Admin;
import app.exceptions.DatabaseException;
import app.persistence.admin.*;
import app.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminMapperTest {
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
                stmt.execute("DELETE FROM admin");
                // Reset the sequence number for customer_id
                stmt.execute("SELECT setval('public.customer_customer_id_seq', 1, false)");

                stmt.execute("INSERT INTO admin ( email, password, role) VALUES " +
                        "('admin@admin.dk', 'admin', 'admin' )");
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
    void testLogInValidCredentials() {
        String email = "admin@admin.dk";
        String password = "admin";

        try {
            Admin actualAdmin = AdminMapper.login(email, password, connectionPool);

            assertNotNull(actualAdmin, "Admin doesn't exist(null)");

            assertEquals(email, actualAdmin.getEmail());
            assertEquals(password, actualAdmin.getPassword());
            assertEquals("admin", actualAdmin.getRole());
        } catch (DatabaseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
