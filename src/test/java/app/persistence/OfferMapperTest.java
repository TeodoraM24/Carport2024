package app.persistence;

import app.entities.Offer;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;


public class OfferMapperTest {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport_test";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    @BeforeEach
    void setUp() {
        try (Connection testConnection = connectionPool.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Remove all rows from relevant tables
                stmt.execute("DELETE FROM customer");
                stmt.execute("DELETE FROM offer");

                // Reset the sequence number for offer
                stmt.execute("SELECT setval('public.offer_offer_id_seq', 1, false)");

                // Insert rows in the correct order: first offer, then customer
                stmt.execute("INSERT INTO offer (offer_id, carport_size, tool_shed_size, cladding_desc, rafter_type_desc, support_beam_desc_size, roof_materials, total_price_with_tax, date) VALUES " +
                        "(1, '10x10x10', '20', 'fkfk', 'eddd', 'Campusvdaej', 'blabla', 2770, '2024-05-14')");
                stmt.execute("INSERT INTO customer (customer_id, first_name, last_name, email, password, phonenumber, address, zip, offer_id) VALUES " +
                        "(1, 'Jon', 'Andersen', 'jon@blabla.com', '1234', 12455, 'Campusvej', 2770, 1)");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('public.customer_customer_id_seq', COALESCE((SELECT MAX(customer_id)+1 FROM public.customer), 1), false)");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            fail("Database connection failed");
        }
    }




    @Test
    void testConnection() throws SQLException {
        assertNotNull(connectionPool.getConnection());
    }

    @Test
    void testGetOfferByCustomerId() throws DatabaseException {
        Offer offer = OfferMapper.getOfferByCustomerId(1, connectionPool);
        assertNotNull(offer);
        assertEquals(1, offer.getOfferId());
        assertEquals("10x10x10", offer.getCarportSize());
        assertEquals("20", offer.getToolShedSize());
        assertEquals("fkfk", offer.getCladdingDesc());
        assertEquals("eddd", offer.getRafterTypeDesc());
        assertEquals("Campusvdaej", offer.getSupportBeamDescSize());
        assertEquals("blabla", offer.getRoofMaterials());
        assertEquals(2770, offer.getTotalPriceWithTax());
    }
    @Test
    void testUpdateOfferStatus() throws DatabaseException, SQLException {
        OfferMapper.updateOfferStatus(1, "Godkend", connectionPool);


        try (Connection connection = connectionPool.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT offer_status FROM offer WHERE offer_id = 1")) {
            rs.next();
            String updatedStatus = rs.getString("offer_status");
            assertEquals("Godkend", updatedStatus);
        }
    }
}
