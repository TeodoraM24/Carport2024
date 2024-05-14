package app.persistence;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import io.javalin.http.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The CustomerRequestMapper class is used for interacting with the database
 */

public class CustomerRequestMapper {

    /**
     * This method retrives all customer requests from the database
     *
     * @param connectionPool The connection to the database
     * @return Returns a list of Customer Requests
     * @throws DatabaseException Handles database error
     */

    public static List<CustomerRequest> getAllCustomerRequest(ConnectionPool connectionPool) throws DatabaseException {
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
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String status = rs.getString("status");
                    String tileType = rs.getString("tile_type");

                    CustomerRequest customerRequest = new CustomerRequest(customerRequestId, length, width, height, tileType, date, status);
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
     * @return Returns a list the requested customer request
     * @throws DatabaseException Handles database error
     */
    public static void makeCustomerRequest(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null) {
            ctx.redirect("/login-page.html");
            ctx.attribute("message", "Du skal logge ind før du kan bestille en forespørgsel");
            return;
        }

        if (currentUser.isHaveRequest()) {
            ctx.redirect("/customer-info-page.html");
            ctx.attribute("message", "Du har allerede bestilt en forespørgsel, vent venligst på vi vender tilbage");
            return;
        }

        LocalDate date = LocalDate.now();
        int height = Integer.parseInt(ctx.formParam("carport-height"));
        int width = Integer.parseInt(ctx.formParam("carport-width"));
        int length = Integer.parseInt(ctx.formParam("carport-length"));

        String insertCustomerRequestQuery = "INSERT INTO customer_request (length, width, height, date) VALUES (?, ?, ?, ?)";
        String insertAdminCustomerRequestQuery = "INSERT INTO admin_customer_request (customer_request_id) VALUES (?)";
        String updateCustomerQuery = "UPDATE customer SET customer_request_id = ? WHERE customer_id = ?";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement insertCustomerRequestStatement = connection.prepareStatement(insertCustomerRequestQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertAdminCustomerRequestStatement = connection.prepareStatement(insertAdminCustomerRequestQuery);
             PreparedStatement updateCustomerStatement = connection.prepareStatement(updateCustomerQuery)) {

            insertCustomerRequestStatement.setInt(1, length);
            insertCustomerRequestStatement.setInt(2, width);
            insertCustomerRequestStatement.setInt(3, height);
            insertCustomerRequestStatement.setDate(4, java.sql.Date.valueOf(date));
            int rowsAffected = insertCustomerRequestStatement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Failed to insert customer request");
            }

            ResultSet rs = insertCustomerRequestStatement.getGeneratedKeys();
            if (!rs.next()) {
                throw new DatabaseException("Failed to retrieve generated key for customer request");
            }
            int customerRequestId = rs.getInt(1);

            insertAdminCustomerRequestStatement.setInt(1, customerRequestId);
            insertAdminCustomerRequestStatement.executeUpdate();

            updateCustomerStatement.setInt(1, customerRequestId);
            updateCustomerStatement.setInt(2, currentUser.getCustomerId());
            updateCustomerStatement.executeUpdate();

            currentUser.setHaveRequest(true);

            ctx.redirect("/carport-offer-sent.html");

        } catch (SQLException e) {
            throw new DatabaseException("Failed to make customer request", e.getMessage());
        }
    }



    /**
     * Handles to delete of a customer request from the database
     *
     * @param customerRequestId The ID of the customer request to delete
     * @param connectionPool    The connection to the database
     * @return Returns a list the requested customer request
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
