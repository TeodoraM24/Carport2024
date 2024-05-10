package app.persistence;

import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerRequestMapper {

    public List<CustomerRequest> getAllCustomerRequest(ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerRequest> customerRequests = new ArrayList<>();
        String sql = "SELECT * FROM customer_request";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int customerRequestId = rs.getInt("customer_request_id");
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String tileType = rs.getString("tile_type");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String status = rs.getString("status");

                    CustomerRequest customerRequest = new CustomerRequest(customerRequestId, length, width, height, tileType, status, date);
                    customerRequests.add(customerRequest);
                }
            }
        } catch (SQLException e) {
            String msg = "Fejl i getAllCustomerRequest()!";
            throw new DatabaseException(msg, e.getMessage());
        }
        return customerRequests;
    }


    public CustomerRequest getCustomerRequestById(int customerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM customer_request WHERE customer_request_id = ?";
        CustomerRequest customerRequest = null;

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerRequestId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String tileType = rs.getString("tile_type");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String status = rs.getString("status");

                    customerRequest = new CustomerRequest(customerRequestId, length, width, height, tileType, status, date);
                }
            }
        } catch (SQLException e) {
            String msg = "Fejl ved getCustomerRequestById. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
        return customerRequest;
    }

    public static void makeCustomerRequest(int length, int width, int height, LocalDate date, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO customer_request (length, width, height, tile_type, date) VALUES (?, ?, ?, ?, ?)";
        String tile_type = "plastik trapez";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, length);
            preparedStatement.setInt(2, width);
            preparedStatement.setInt(3, height);
            preparedStatement.setString(4, tile_type);
            preparedStatement.setDate(5, java.sql.Date.valueOf(date));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af forspørgelse");
            }
        } catch (SQLException e) {
            String msg = "Fejl ved makeCustomerRequest. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static void deleteCustomerRequest(int customerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM customer_request WHERE customer_request_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerRequestId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved sletning af forespørgelsen");
            }
        } catch (SQLException e) {
            String msg = "Fejl, prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static void updateCustomerRequest(int customerRequestId, int length, int width, int height, LocalDate date, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customer_request SET length=?, width=?, height=?, tile_type=?, date=?, status=? WHERE customer_request_id=?";
        String tile_type = "plastik trapez";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, length);
            preparedStatement.setInt(2, width);
            preparedStatement.setInt(3, height);
            preparedStatement.setString(4, tile_type);
            preparedStatement.setDate(5, java.sql.Date.valueOf(date));
            preparedStatement.setString(6, status);
            preparedStatement.setInt(7, customerRequestId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af forspørgelse");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

}
