package app.persistence;

import app.entities.Offer;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OfferMapper {

    public static Offer getOfferByCustomerId(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM offer o JOIN customer c ON o.offer_id = c.offer_id WHERE c.customer_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int offerId = rs.getInt("offer_id");
                String carportSize = rs.getString("carport_size");
                String toolShedSize = rs.getString("tool_shed_size");
                String claddingDesc = rs.getString("cladding_desc");
                String rafterTypeDesc = rs.getString("rafter_type_desc");
                String supportBeamDescSize = rs.getString("support_beam_desc_size");
                String roofMaterials = rs.getString("roof_materials");
                double totalPriceWithTax = rs.getDouble("total_price_with_tax");

                return new Offer(offerId, carportSize, toolShedSize, claddingDesc, rafterTypeDesc, supportBeamDescSize, roofMaterials, totalPriceWithTax);
            } else {
                throw new DatabaseException("Offer not found for customer with id: " + customerId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving offer from the database: " + e.getMessage());
        }
    }

    public static void updateOfferStatus(int offerId, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE offer SET offer_status = ? WHERE offer_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, offerId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DatabaseException("No offer found with id: " + offerId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error updating offer status in the database: " + e.getMessage());
        }
    }
}


