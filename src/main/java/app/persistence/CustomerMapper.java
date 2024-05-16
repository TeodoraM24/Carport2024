package app.persistence;

import app.entities.Admin;
import app.entities.Customer;
import app.exceptions.DatabaseException;

import java.sql.*;

public class CustomerMapper {
    public static Customer logInd(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM customer WHERE email=? AND password=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                email = rs.getString("email");
                password = rs.getString("password");
                String address = rs.getString("address");
                int zip = rs.getInt("zip");
                int phoneNumber = rs.getInt("phonenumber");
                String role = rs.getString("role");


                return new Customer(customerId, email, password, phoneNumber, firstName, lastName, address, zip, role);
            } else {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static int createUser(String email, String password, String firstName, String lastName, int zip, String address, int phoneNumber, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO customer (email, password, first_name, last_name, zip, address, phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setInt(5, zip);
            ps.setString(6, address);
            ps.setInt(7, phoneNumber);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Fejl ved oprettelse af ny bruger");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "E-mailen findes allerede. Vælg en anden";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}
