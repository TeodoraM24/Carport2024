package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;

public class OfferMapper {
    public static int addPrice(double purchasePrice, double salesPrice, double coverage, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO price (purchase_price, salesprice_with_tax, coverage_ratio) VALUES (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setDouble(1, purchasePrice);
            ps.setDouble(2, salesPrice);
            ps.setDouble(3, coverage);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Could not add new price to price table");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: INSERT error in addPriceOffer", e.getMessage());

        }
    }

    public static int addOffer(String rafterDescription, String supportBeamDescription, String tileType, LocalDate currentDate, int partsListId, int priceId, int customerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO offer (rafter_type_desc, support_beam_desc_size, roof_materials, date, parts_list_id, price_id, customer_request_id) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, rafterDescription);
            ps.setString(2,supportBeamDescription);
            ps.setString(3,tileType);
            ps.setDate(4, Date.valueOf(currentDate));
            ps.setInt(5,partsListId);
            ps.setInt(6,priceId);
            ps.setInt(7,customerRequestId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                return -1;
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB: INSERT error in addPriceOffer", e.getMessage());
        }
    }
}
