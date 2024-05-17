package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.*;
import java.time.LocalDate;

public class PurchaseMapper {
    public static int addCarport(int requestWidth, int requestHeight, int requestLength, int priceId, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO carport (width, height, length, price_id) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, requestWidth);
            ps.setInt(2, requestHeight);
            ps.setInt(3, requestLength);
            ps.setInt(4, priceId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Could not add a new carport");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL INSERT error for inserting to carport table", e.getMessage());
        }
    }

    public static int addInvoice(int partsListId, LocalDate currentDate, int carportId, ConnectionPool connectionPool) throws DatabaseException{
        String sql = "INSERT INTO invoice (parts_list_id, date, carport_id) VALUES (?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, partsListId);
            ps.setDate(2, Date.valueOf(currentDate));
            ps.setInt(3, carportId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Could not add a new invoice");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL INSERT error for inserting to invoice", e.getMessage());
        }
    }

    public static int addInvoiceToCustomer(int customerId, int invoiceId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "INSERT INTO customer_invoice (customer_id, invoice_id) VALUES (?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, customerId);
            ps.setInt(2, invoiceId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            } else {
                throw new DatabaseException("Could not attach the invoice to the customer");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL INSERT error for inserting to customer_invoice table", e.getMessage());
        }
    }
}
