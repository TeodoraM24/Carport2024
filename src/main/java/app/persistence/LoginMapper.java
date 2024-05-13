package app.persistence;

import app.entities.Admin;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginMapper {
    public static boolean isAdmin(String email, ConnectionPool connectionPool) throws  DatabaseException{
        String sql = "SELECT * FROM public.\"admin\" WHERE email=?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                return true;
            } else {
                return false;
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
    }
}
