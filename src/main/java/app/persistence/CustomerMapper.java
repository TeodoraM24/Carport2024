package app.persistence;

import app.entities.Customer;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {
    public static Customer logInd(String email, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "select * from public.\"users\" where email=? and password=?"; //NAVN ÆNDRINGER

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("customerId");
                String role = rs.getString("role");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String address = rs.getString("address");
                int zip = rs.getInt("zip");
                boolean haveRequest = rs.getBoolean("haveRequest");
                int phoneNumber=rs.getInt("phoneNumber");


                return new Customer(id, email, password, phoneNumber, firstName, lastName, address, zip, role, haveRequest);
            } else
            {
                throw new DatabaseException("Fejl i login. Prøv igen");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }

    public static void createUser(String email, String password, String firstName, String lastName, int zip, int phoneNumber, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO users (email, password, firstName, lastName, zip, phoneNumber) VALUES (?, ?, ?, ?, ?, ?)"; //DER SKAL ÆNDRES NAVNE

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setInt(5, zip);
            ps.setInt(6, phoneNumber);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
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
