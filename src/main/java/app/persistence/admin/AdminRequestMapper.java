package app.persistence.admin;

import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdminRequestMapper {

    public static int getCustomerRequestId(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT customer_request_id FROM customer WHERE customer_id = ?;";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("customer_request_id");
            } else {
                throw new DatabaseException("Failed at retrieving chosen customer's request id.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error in getCustomerRequestId() in AdminRequestMapper. ", e.getMessage());
        }
    }


    public static CustomerRequest getCustomerRequest(int requestId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT c.customer_request_id, length, width, height, tile_type, date, status  FROM customer c " +
                "INNER JOIN customer_request USING(customer_request_id) WHERE customer_request_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, requestId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int customerRequestId = rs.getInt("customer_request_id");
                int length = rs.getInt("length");
                int width = rs.getInt("width");
                int height = rs.getInt("height");
                String tileType = rs.getString("tile_type");
                LocalDate date = rs.getDate("date").toLocalDate();
                String status = rs.getString("status");

                return new CustomerRequest(customerRequestId, length, width, height, tileType, date, status);
            } else {
                throw new DatabaseException("Failed at retrieving chosen customer's request.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error in getCustomerRequest() in AdminRequestMapper. ", e.getMessage());
        }
    }

    public static void updateCustomerRequest(int customerRequestId, int requestLength, int requestWidth, int requestHeight, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customer_request SET length = ?, width = ?, height = ? WHERE customer_request_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, requestLength);
            ps.setInt(2, requestWidth);
            ps.setInt(3, requestHeight);
            ps.setInt(4, customerRequestId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL UPDATE error in updateCustomerRequest() in AdminRequestMapper. ", e.getMessage());
        }
    }

    public static void updateCustomerRequestStatus(int customerRequestId, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customer_request SET status = ? WHERE customer_request_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setString(1, status);
            ps.setInt(2, customerRequestId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL UPDATE error in updateCustomerRequestStatus() in AdminRequestMapper. ", e.getMessage());
        }
    }
}
