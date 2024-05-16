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


    public static List<CustomerRequest> getAllCustomerRequests(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerRequest> customerRequests = new ArrayList<>();
        String sql = "SELECT * FROM customer_request WHERE customer_request_id = ?";


        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerRequestId = rs.getInt("customer_request_id");
                int length = rs.getInt("length");
                int height = rs.getInt("height");
                int width = rs.getInt("width");
                String tileType = rs.getString("tile_type");
                java.sql.Date sqlDate = rs.getDate("date");
                LocalDate date = sqlDate.toLocalDate();
                String status = rs.getString("status");

                CustomerRequest customerRequest = new CustomerRequest(customerRequestId, length, width, height, tileType, date, status);
                customerRequests.add(customerRequest);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving customer requests from the database", e.getMessage());
        }
        return customerRequests;
    }
}
