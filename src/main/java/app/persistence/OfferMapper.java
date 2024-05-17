package app.persistence;

import app.entities.Offer;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OfferMapper {

    public static Offer getOfferByCustomerId(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT o.offer_id, c.customer_id, cq.length, cq.width, cq.height, o.rafter_type_desc, o.support_beam_desc_size, o.roof_materials, p.salesprice_with_tax, o.status FROM offer o INNER JOIN customer c USING(customer_request_id) INNER JOIN customer_request cq USING(customer_request_id) INNER JOIN price p USING(price_id) INNER JOIN parts_list pl USING(parts_list_id) WHERE customer_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int offerId = rs.getInt("offer_id");
                int length= rs.getInt("length");
                int height= rs.getInt("height");
                int width=rs.getInt("width");
                String carportSize = width + "x" + length + "x" + height;
                String rafterTypeDesc = rs.getString("rafter_type_desc");
                String supportBeamDescSize = rs.getString("support_beam_desc_size");
                String roofMaterials = rs.getString("roof_materials");
                double totalPriceWithTax = rs.getDouble("salesprice_with_tax");
                String status = rs.getString("status");

                return new Offer(offerId, carportSize, rafterTypeDesc, supportBeamDescSize, roofMaterials, totalPriceWithTax, status);

            } else {
                throw new DatabaseException("Offer not found for customer with id: " + customerId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving offer from the database: " + e.getMessage());
        }
    }

    public static void updateOfferStatus(int offerId, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE offer SET status = ? WHERE offer_id = ?";
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


