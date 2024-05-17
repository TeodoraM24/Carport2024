package app.persistence;

import app.entities.Offer;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                throw new DatabaseException("Could not get offer for given customer id.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error when trying to select an offer from offer table");
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
                throw new DatabaseException("Could not set new status for given offer id.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL UPDATE error when trying to update an offer from offer table");
        }
    }

    public static List<Offer> getAllCustomerOffers(ConnectionPool connectionPool) throws DatabaseException {
        List<Offer> offers = new ArrayList<>();

        String sql = "SELECT o.offer_id, c.customer_id, cq.length, cq.width, cq.height, o.rafter_type_desc, o.support_beam_desc_size, o.roof_materials, p.salesprice_with_tax, o.status FROM offer o INNER JOIN customer c USING(customer_request_id) INNER JOIN customer_request cq USING(customer_request_id) INNER JOIN price p USING(price_id) INNER JOIN parts_list pl USING(parts_list_id)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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

                offers.add(new Offer(offerId, carportSize, rafterTypeDesc, supportBeamDescSize, roofMaterials, totalPriceWithTax, status));
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error when trying to select an offer from offer table");
        }
        return offers;
    }

    public static void updateCustomerOffer(int offerId, int customerId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customer SET offer_id = ? WHERE customer_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setInt(1, offerId);
             ps.setInt(2, customerId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DatabaseException("Could not update customer's offer id.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL UPDATE error when trying to update the offer_id from customer table");
        }
    }

    public static Offer getOfferById(int offerId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT o.offer_id, c.customer_id, o.parts_list_id, o.price_id, o.customer_request_id, cq.length, cq.width, cq.height, o.rafter_type_desc, o.support_beam_desc_size, o.roof_materials, p.salesprice_with_tax, o.status FROM offer o INNER JOIN customer c USING(customer_request_id) INNER JOIN customer_request cq USING(customer_request_id) INNER JOIN price p USING(price_id) INNER JOIN parts_list pl USING(parts_list_id) WHERE o.offer_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, offerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("offer_id");
                int length= rs.getInt("length");
                int height= rs.getInt("height");
                int width=rs.getInt("width");
                String carportSize = width + "x" + length + "x" + height;
                String rafterTypeDesc = rs.getString("rafter_type_desc");
                String supportBeamDescSize = rs.getString("support_beam_desc_size");
                String roofMaterials = rs.getString("roof_materials");
                double totalPriceWithTax = rs.getDouble("salesprice_with_tax");
                String status = rs.getString("status");
                int priceId = rs.getInt("price_id");
                int partsListId = rs.getInt("parts_list_id");
                int customerRequestId = rs.getInt("customer_request_id");

                return new Offer(id, carportSize, rafterTypeDesc, supportBeamDescSize, roofMaterials, totalPriceWithTax, status, partsListId, priceId, customerRequestId);
            } else {
                throw new DatabaseException("Offer not found for given offer id.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error when trying to select an offer from offer table");
        }
    }

    public static void deleteOffer(int offerId, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "DELETE FROM offer WHERE offer_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, offerId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Could not delete offer from offer table");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL DELETE error when trying to delete an offer from offer table", e.getMessage());
        }
    }
}


