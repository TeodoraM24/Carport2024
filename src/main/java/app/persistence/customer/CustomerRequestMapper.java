package app.persistence.customer;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

/**
 * The CustomerRequestMapper class is used for interacting with the database
 */

public class CustomerRequestMapper {

    public static List<CustomerRequest> getAllCustomerRequests(int customerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerRequest> customerRequests = new ArrayList<>();
        String sql = "SELECT * FROM customer_request WHERE customer_request_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customerRequestId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int length = rs.getInt("length");
                int height = rs.getInt("height");
                int width = rs.getInt("width");
                String tileType = rs.getString("tile_type");
                Date sqlDate = rs.getDate("date");
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

    /**
     * This method retrives all customer requests from the database
     *
     * @param connectionPool The connection to the database
     * @return Returns a list of Customer Requests
     * @throws DatabaseException Handles database error
     */
    public static List<CustomerRequest> getAllCustomerRequest(ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerRequest> customerRequests = new ArrayList<>();
        String sql = "SELECT cr.customer_request_id, cr.length, cr.width, cr.height, cr.date, cr.status, cr.tile_type, c.first_name, c.last_name, c.customer_id " +
                "FROM customer_request cr " +
                "JOIN customer c ON cr.customer_request_id = c.customer_request_id";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int customerRequestId = rs.getInt("customer_request_id");
                    int requestLength = rs.getInt("length");
                    int requestWidth = rs.getInt("width");
                    int requestHeight = rs.getInt("height");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String status = rs.getString("status");
                    String tileType = rs.getString("tile_type");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    int customerId = rs.getInt("customer_id");

                    Customer customer = new Customer(customerId, firstName, lastName);
                    CustomerRequest customerRequest = new CustomerRequest(customerRequestId, requestLength, requestWidth, requestHeight, tileType, date, status, customer);
                    customerRequests.add(customerRequest);
                }
            }
        } catch (SQLException e) {
            String msg = "Fejl i getAllCustomerRequest()!";
            throw new DatabaseException(msg, e.getMessage());
        }
        return customerRequests;
    }

    /**
     * Getting a customer request from the database based on the customer request id
     *
     * @param customerRequestId The ID of the customer request to retrieve
     * @param connectionPool    The connection to the database
     * @return Returns a list the requested customer request
     * @throws DatabaseException Handles database error
     */
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
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String status = rs.getString("status");
                    String tileType = rs.getString("tile_type");

                    customerRequest = new CustomerRequest(customerRequestId, length, width, height, tileType, date, status);
                }
            }
        } catch (SQLException e) {
            String msg = "Fejl ved getCustomerRequestById. Prøv igen";
            throw new DatabaseException(msg, e.getMessage());
        }
        return customerRequest;
    }

    /**
     * Handles the insert into the database
     *
     * @param connectionPool The connection to the database
     * @throws DatabaseException Handles database error
     */

    public static int makeCustomerRequest(Customer currentUser, int height, int width, int length, LocalDate date, ConnectionPool connectionPool) throws DatabaseException {
        if (customerIdAlreadyHasRequest(connectionPool, currentUser.getCustomer_request_id())) {
            throw new DatabaseException("Der findes allerede en forspørgsel");
        }

        String insertCustomerRequestQuery = "INSERT INTO customer_request (length, width, height, date) VALUES (?, ?, ?, ?)";
        String updateCustomerQuery = "UPDATE customer SET customer_request_id = ? WHERE customer_id = ? AND customer_request_id IS NULL";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement insertCustomerRequestStatement = connection.prepareStatement(insertCustomerRequestQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement updateCustomerStatement = connection.prepareStatement(updateCustomerQuery)) {

            insertCustomerRequestStatement.setInt(1, length);
            insertCustomerRequestStatement.setInt(2, width);
            insertCustomerRequestStatement.setInt(3, height);
            insertCustomerRequestStatement.setDate(4, java.sql.Date.valueOf(date));
            int rowsAffected = insertCustomerRequestStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Database fejl: forespørgsel blev ikke indsat rigtigt");
            }

            ResultSet rs = insertCustomerRequestStatement.getGeneratedKeys();
            if (!rs.next()) {
                throw new DatabaseException("Kunne ikke hente genereret nøgle til forespørgsel");
            }

            int customerRequestId = rs.getInt(1);

            updateCustomerStatement.setInt(1, customerRequestId);
            updateCustomerStatement.setInt(2, currentUser.getCustomerId());
            int updatedRows = updateCustomerStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new DatabaseException("Fejl ved opdatering af forespørgsel eller der findes allerede en forespørgsel");
            }
            return customerRequestId;

        } catch (SQLException e) {
            throw new DatabaseException("Fejl under oprettelse", e.getMessage());
        }
    }

    private static boolean customerIdAlreadyHasRequest(ConnectionPool connectionPool, int customerId) throws DatabaseException {
        String sql = "SELECT customer_request_id FROM customer WHERE customer_id = ? AND customer_request_id IS NOT NULL";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved tjek om forespørgsel allerede findes", e.getMessage());
        }
    }

    /**
     * Handles to delete of a customer request from the database
     *
     * @param customerRequestId The ID of the customer request to delete
     * @param connectionPool    The connection to the database
     * @throws DatabaseException Handles database error
     */
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

    /**
     * Grant the ability to change the attribute of the customer request
     *
     * @param customerId     The ID of the customer request
     * @param length         Length of the carport
     * @param width          Width of the carport
     * @param height         Height of the carport
     * @param date           Date the request was made
     * @param status         Status of the request
     * @param connectionPool The connection to the database
     * @throws DatabaseException Handles database error
     */
    public static void updateCustomerRequest(int customerId, int length, int width, int height, LocalDate date, String status, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "UPDATE customer_request SET length=?, width=?, height=?, date=?, status=? WHERE customer_request_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, length);
            preparedStatement.setInt(2, width);
            preparedStatement.setInt(3, height);
            preparedStatement.setDate(4, java.sql.Date.valueOf(date));
            preparedStatement.setString(5, status);
            preparedStatement.setInt(6, customerId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved opdatering af kundeforespørgsel");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen (updateCustomerRequest)";
            throw new DatabaseException(msg, e.getMessage());
        }
    }
}
