package app.persistence.admin;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {

    /***
     * Gets all the materials (description, height, width, length and price) from the database table material and returns a List of Material
     *
     * @param connectionPool ConnectionPool used to execute sql for retrieving all materials
     * @return list of object of type Material
     * @throws DatabaseException Displays "Fejl under indhentelse af materialer" + system msg
     */
    public static List<Material> getAllMaterials(ConnectionPool connectionPool) throws DatabaseException {

        List<Material> materials = new ArrayList<>();

        String sql = "SELECT * FROM material";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int materialId = rs.getInt("material_id");
                String description = rs.getString("material_description");
                int height = rs.getInt("height");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                materials.add(new Material(materialId, description, height, width, length, price));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl under indhentelse af materialer", e.getMessage());
        }
        return materials;
    }

    /***
     * Gets a specific material by material id with all data (description, height, width,
     * length and price) from the database table material and returns an object type of Material
     *
     * @param materialId used to get a specific material by id
     * @param connectionPool ConnectionPool used to execute sql for retrieving a specific material
     * @return an object type of Material
     * @throws DatabaseException Displays "Fejl ved indhentelse af task med id: (material id)" + system msg
     */
    public static Material getMaterialById(int materialId, ConnectionPool connectionPool) throws DatabaseException {

        Material material = null;

        String sql = "SELECT * FROM material WHERE material_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("material_id");
                String description = rs.getString("material_description");
                int height = rs.getInt("height");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                material = new Material(id, description, height, width, length, price);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved indhentelse af task med id: " + materialId, e.getMessage());
        }
        return material;
    }

    /**
     * Adds the user input Material to material table in database
     *
     * @param connectionPool  ConnectionPool used to insert cupcake id in database table basket
     * @throws DatabaseException Displays "Fejl under indsætning af materiale:" + system msg and "Fejl i DB connection" + system msg
     */
    public static int addMaterial(String description, int height, int width, int length, double price, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO material (material_description, height, width, length, price) VALUES (?,?,?,?,?)" +
                "ON CONFLICT (material_description, height, width, length, price) DO NOTHING";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, description);
            ps.setInt(2, height);
            ps.setInt(3, width);
            ps.setInt(4, length);
            ps.setDouble(5, price);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i DB connection", e.getMessage());
        }
    }

    /***
     * Deletes the chosen material from material table in database
     *
     * @param materialId the material id used to specify which material needs to be deleted
     * @param connectionPool  ConnectionPool used to delete material in material table in database
     * @throws DatabaseException Displays "Fejl i opdatering af materiale" + system msg and "Fejl ved sletning af materiale" + system msg
     */
    public static void deleteMaterial(int materialId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "DELETE FROM material WHERE material_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, materialId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl i opdatering af en materiale");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved sletning af en materiale", e.getMessage());
        }
    }

    /***
     * Updates the fields (materials_description, height, width, length and price) on a specified material in material table in database
     *
     * @param materialId used to specify which material will be updated
     * The input used to replace the different column values on the specified material
     * @param description
     * @param height
     * @param width
     * @param length
     * @param price
     * @param connectionPool  ConnectionPool used to update material in material table in database
     * @throws DatabaseException Displays "Fejl i opdatering af en materiale" + system msg
     */
    public static void updateMaterial(int materialId, String description, int height, int width, int length, double price, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE material SET material_description = ?, height = ?, width = ?, length = ?, price = ? WHERE material_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, description);
            ps.setInt(2, height);
            ps.setInt(3, width);
            ps.setInt(4, length);
            ps.setDouble(5, price);
            ps.setInt(6, materialId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Error updating material");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error updating material: " + e.getMessage());
        }
    }

    public static int getMaterialIdByData(String description, int height, int width, int length, double price, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "SELECT material_id FROM material WHERE material_description = ? " +
                "AND height = ? AND width = ? AND length = ? AND price = ?;";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setString(1, description);
            ps.setInt(2, height);
            ps.setInt(3, width);
            ps.setInt(4, length);
            ps.setDouble(5, price);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("material_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error in getMaterialIdByData", e.getMessage());
        }
    }
}
