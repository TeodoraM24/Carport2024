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

    public List<CustomerRequest> getCustomerRequest(int customer_request_id, ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerRequest> customerRequests = new ArrayList<>();
        String sql = "SELECT * FROM customer_request WHERE customer_request_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customer_request_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int length = rs.getInt("length");
                    int width = rs.getInt("width");
                    int height = rs.getInt("height");
                    String tileType = rs.getString("tile_type");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String status = rs.getString("status");

                    CustomerRequest customerRequest = new CustomerRequest(customer_request_id, length, width, height, tileType, status, date);
                    customerRequests.add(customerRequest);
                }
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
        return customerRequests;
    }

    public static void makeCustomerRequest(CustomerRequest customerRequest, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO customer_request (length, width, height, tileType, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerRequest.getLength());
            preparedStatement.setInt(2, customerRequest.getWidth());
            preparedStatement.setInt(3, customerRequest.getHeight());
            preparedStatement.setString(4, "plast trapez");
            preparedStatement.setString(5, String.valueOf(java.sql.Date.valueOf(customerRequest.getDate())));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af forspørgelse");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public void deleteCustomerRequest(int customerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "DELETE FROM customer_request WHERE customer_request_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerRequestId);
            preparedStatement.executeUpdate();

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved sletning af forespørgelsen");
            }
        }catch (SQLException e){
            String msg = "Fejl, prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    private void updateCustomerRequest(CustomerRequest customerRequest, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customer_request SET length=?, width=?, height=?, tileType=?, date=?, status=? WHERE customer_request_id=?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerRequest.getLength());
            preparedStatement.setInt(2, customerRequest.getWidth());
            preparedStatement.setInt(3, customerRequest.getHeight());
            preparedStatement.setString(4, customerRequest.getTileType());
            preparedStatement.setString(5, String.valueOf(java.sql.Date.valueOf(customerRequest.getDate())));
            preparedStatement.setString(6, customerRequest.getStatus());
            preparedStatement.setInt(7, customerRequest.getCustomerRequestId());

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
