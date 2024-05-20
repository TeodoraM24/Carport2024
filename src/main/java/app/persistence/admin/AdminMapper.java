package app.persistence.admin;

import app.entities.Admin;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper {

    public static Admin login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM public.\"admin\" WHERE email=? AND password=?";

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
                int id = rs.getInt("admin_id");
                String role = rs.getString("role");

                return new Admin(id,email,password,role);
            } else {
                throw new DatabaseException("Failed to log in as an admin with given email and password.");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("DB: SELECT error in login() in AdminMapper. ", e.getMessage());
        }
    }
}

