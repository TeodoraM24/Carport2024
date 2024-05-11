package app.persistence;

import app.entities.Material;
import app.entities.PartsListItem;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartsListItemMapper {
    public static void addPartsListItem(int materialId, int amount, String instruction, int unit, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO parts_list_item (material_id, amount, instruction_description, unit_id) VALUES (?,?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, materialId);
            ps.setInt(2, amount);
            ps.setString(3, instruction);
            ps.setInt(4, unit);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("DB: INSERT error in addPartsListItem", e.getMessage());
        }
    }

    public static int getPartsListItemIdByData(int materialId, int amount, String instruction, int unitId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT parts_list_item_id FROM parts_list_item WHERE material_id = ? " +
                "AND amount = ? AND instruction_description = ? AND unit_id = ?;";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, materialId);
            ps.setInt(2, amount);
            ps.setString(3, instruction);
            ps.setInt(4, unitId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("parts_list_item_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error in getPartsListIdByData", e.getMessage());
        }
    }

    public static List<PartsListItem> getAllPartsListItems(ConnectionPool connectionPool) throws DatabaseException {

        List<PartsListItem> partsListItems = new ArrayList<>();

        String sql = "SELECT * FROM parts_list_item INNER JOIN material USING(material_id) INNER JOIN unit USING(unit_id)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int partsListItemId = rs.getInt("parts_list_item_id");
                int materialId = rs.getInt("material_id");
                int amount = rs.getInt("amount");
                String instruction = rs.getString("instruction_description");
                String unitType = rs.getString("unit_name");

                Material material = MaterialMapper.getMaterialById(materialId, connectionPool);
                partsListItems.add(new PartsListItem(partsListItemId, material, amount, unitType, instruction));
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error in getAllPartsListItems", e.getMessage());
        }
        return partsListItems;
    }
}
