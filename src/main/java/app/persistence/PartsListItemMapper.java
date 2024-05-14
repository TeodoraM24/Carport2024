package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.*;

public class PartsListItemMapper {
    public static int addPartsListItem(int materialId, int amount, String instruction, String unit, double totalPrice, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO parts_list_item (material_id, amount, instruction_description, unit, total_price) VALUES (?,?,?,?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, materialId);
            ps.setInt(2, amount);
            ps.setString(3, instruction);
            ps.setString(4, unit);
            ps.setDouble(5, totalPrice);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Could not add new parts list item to parts_list_item table.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: INSERT error in addPartsListItem", e.getMessage());
        }
    }
}
