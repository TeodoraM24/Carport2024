package app.persistence.admin;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

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
                throw new DatabaseException("Failed to insert a new carport to carport table.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL INSERT error in addCarport() in PurchaseMapper. ", e.getMessage());
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
                throw new DatabaseException("Failed to insert a new invoice to invoice table.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL INSERT error in addInvoice() in PurchaseMapper. ", e.getMessage());
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
                throw new DatabaseException("Failed to attach the invoice to the customer in customer invoice table.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL INSERT error in addInvoiceToCustomer() in PurchaseMapper. ", e.getMessage());
        }
    }
}
