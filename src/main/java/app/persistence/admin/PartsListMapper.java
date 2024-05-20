package app.persistence.admin;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.*;

public class PartsListMapper {
    public static int addPartsList(int priceId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO parts_list (price_id) VALUES (?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, priceId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Failed to insert new price to price table.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: INSERT error in addPartsList() in PartsListMapper. ", e.getMessage());

        }
    }

    public static int addPartsListItem(int partsListId, int partsListItemId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO parts_list_parts_list_item (parts_list_id, parts_list_item_id) VALUES (?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, partsListId);
            ps.setInt(2, partsListItemId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Failed to insert new price to price table.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: INSERT error in addPartsListItem() in PartsListMapper. ", e.getMessage());

        }
    }
}
