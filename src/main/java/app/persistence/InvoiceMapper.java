package app.persistence;

import app.entities.Invoice;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class InvoiceMapper {
    public static List<Invoice> getACustomersInvoice(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        List<Invoice> listOfCustomersInvoices = new ArrayList<>();
        String sql = "SELECT " +
                "c.customer_id," +
                "p.parts_list_id," +
                "p.amount," +
                "p.unit_id," +
                "p.instruction_description," +
                "i.invoice_id," +
                "i.date AS invoice_date" +
                "FROM customer c" +
                "JOIN customer_carport cc ON c.customer_id = cc.customer_id\n" +
                "JOIN carport_parts_list cpl ON cc.carport_id = cpl.carport_id\n" +
                "JOIN parts_list p ON cpl.parts_list_id = p.parts_list_id\n" +
                "JOIN invoice i ON p.parts_list_id = i.parts_list_id;" +
                "WHERE c.customer_id =?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int invoiceId = rs.getInt("order_id");
                LocalDate date = rs.getDate("invoice_date").toLocalDate();
                Invoice invoice = new Invoice(customerId, invoiceId, date);
                listOfCustomersInvoices.add(invoice);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i SQL!!", e.getMessage());
        }
        return listOfCustomersInvoices;
    }

}




