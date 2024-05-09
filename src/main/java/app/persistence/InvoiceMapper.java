package app.persistence;

import app.entities.CustomerPartslist;
import app.entities.Invoice;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class InvoiceMapper {

    /***
     * Gets a customers invoice (order_id and date) from the database by joining customer_carport, carport_parts_list and invoice table
     * and returns a List of Customers Invoices
     *
     * @param connectionPool ConnectionPool used to execute sql for retrieving all materials
     * @return list of object of type Invoice
     * @throws DatabaseException Displays "Fejl under indhentelse af materialer" + system msg
     */
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


    /**
     * Retrieves a customer's parts list ( material description, length, amount, unit name, and instruction description)
     * from the database by joining the customer_invoice, invoice and parts_list tables.
     * Returns a List of CustomerPartslist objects representing the customer's parts list for a specific invoice.
     *
     * @param customerId     The ID of the customer for whom to retrieve the parts list
     * @param connectionPool The ConnectionPool used to execute SQL queries
     * @return A list of CustomerPartslist objects representing the customer's parts list
     * @throws DatabaseException If an error occurs while retrieving the parts list, displays "Fejl under indhentelse af materialer" followed by the system message
     */

    public static List<CustomerPartslist> getACustomersPartlist(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerPartslist> listOfCustomerPartlist = new ArrayList<>();
        String sql = "SELECT m.material_description, m.length, pl.amount, u.unit_name, pl.instruction_description\n" +
                "FROM customer AS c\n" +
                "INNER JOIN customer_invoice AS ci ON c.customer_id = ci.customer_id\n" +
                "INNER JOIN invoice AS i ON ci.invoice_id = i.invoice_id\n" +
                "INNER JOIN parts_list AS pl ON i.parts_list_id = pl.parts_list_id\n" +
                "LEFT JOIN parts_list_material AS plm ON pl.parts_list_id = plm.parts_list_id\n" +
                "LEFT JOIN material AS m ON plm.material_id = m.material_id\n" +
                "LEFT JOIN unit AS u ON pl.unit_id = u.unit_id\n" +
                "WHERE c.customer_id = ? AND i.invoice_id = ?;";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String materialDescription = rs.getString("material_description");
                int length = rs.getInt("length");
                int amount = rs.getInt("amount");
                String unitName = rs.getString("unit_name");
                String description = rs.getString("instruction_description");

                listOfCustomerPartlist.add(new CustomerPartslist(materialDescription, length, amount, unitName, description, customerId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i SQL!!", e.getMessage());
        }
        return listOfCustomerPartlist;
    }


}




