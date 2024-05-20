package app.persistence.customer;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class InvoiceMapper {

    /***
     * Retrieves the order history of a customer.
     * Retrieves a list of invoices associated with the specified customer ID.
     *
     * @param customerId The unique identifier of the customer whose order history is to be retrieved
     * @param connectionPool The ConnectionPool object used to establish a database connection
     * @return A List of Invoice objects representing the order history of the customer
     * @throws DatabaseException Thrown if there's an error in executing the SQL query
     */

    public static List<Invoice> getCustomersOrderHistory(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        List<Invoice> listOfCustomersInvoices = new ArrayList<>();
        String sql = "SELECT customer_invoice.customer_id, invoice.invoice_id, invoice.date\n" +
                "FROM public.customer_invoice\n" +
                "INNER JOIN public.invoice ON customer_invoice.invoice_id = invoice.invoice_id\n" +
                "WHERE customer_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Date date = rs.getDate("date");
                int invoiceId = rs.getInt("invoice_id");
                Invoice invoice = new Invoice(invoiceId, customerId, date.toLocalDate());
                listOfCustomersInvoices.add(invoice);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i SQL!!", e.getMessage());
        }
        return listOfCustomersInvoices;
    }

    /***
     * Retrieves the details of an invoice for a specific customer.
     * Retrieves a list of InvoiceDetails associated with the specified invoice ID and customer ID.
     *
     * @param invoiceId The unique identifier of the invoice for which details are to be retrieved
     * @param customerId The unique identifier of the customer associated with the invoice
     * @param connectionPool The ConnectionPool object used to establish a database connection
     * @return A List of InvoiceDetails objects representing the details of the invoice for the specified customer
     * @throws DatabaseException Thrown if there's an error in executing the SQL query
     */

    public static List<InvoiceDetails> getCustomerInvoiceDetails(int customerId, int invoiceId, ConnectionPool connectionPool) throws DatabaseException {
        List<InvoiceDetails> listOfInvoiceDetails = new ArrayList<>();
        String sql = "SELECT\n" +
                "    customer.customer_id,\n" +
                "\tinvoice.invoice_id,\n" +
                "    material.material_description,\n" +
                "    material.length,\n" +
                "    parts_list_item.amount,\n" +
                "    parts_list_item.unit,\n" +
                "    parts_list_item.instruction_description\n" +
                "FROM\n" +
                "    public.customer\n" +
                "INNER JOIN\n" +
                "    public.customer_invoice ON customer.customer_id = customer_invoice.customer_id\n" +
                "INNER JOIN\n" +
                "    public.invoice ON customer_invoice.invoice_id = invoice.invoice_id\n" +
                "INNER JOIN\n" +
                "    public.parts_list ON invoice.parts_list_id = parts_list.parts_list_id\n" +
                "INNER JOIN\n" +
                "    public.parts_list_parts_list_item ON parts_list.parts_list_id = parts_list_parts_list_item.parts_list_id\n" +
                "INNER JOIN\n" +
                "\tpublic.parts_list_item ON parts_list_parts_list_item.parts_list_item_id = parts_list_item.parts_list_item_id\n" +
                "INNER JOIN\n" +
                "    public.material ON parts_list_item.material_id = material.material_id\n" +
                "WHERE\n" +
                "    customer.customer_id = ?" +
                "    AND invoice.invoice_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ps.setInt(2, invoiceId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String materialDescription = rs.getString("material_description");
                int length = rs.getInt("length");
                int amount = rs.getInt("amount");
                String unit = rs.getString("unit");
                String description = rs.getString("instruction_description");

                listOfInvoiceDetails.add(new InvoiceDetails(materialDescription, length, amount, unit, description, customerId, invoiceId));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl i SQL!!", e.getMessage());
        }
        return listOfInvoiceDetails;
    }

    public static Carport getCarport(int invoiceId, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT carport_id, invoice_id, width, height, length, price_id, purchase_price, salesprice_with_tax, coverage_ratio FROM public.invoice INNER JOIN carport USING(carport_id) INNER JOIN price USING(price_id) WHERE invoice_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, invoiceId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int carportId = rs.getInt("carport_id");
                int width = rs.getInt("width");
                int height = rs.getInt("height");
                int length = rs.getInt("length");
                int priceId = rs.getInt("price_id");
                double purchasePrice = rs.getDouble("purchase_price");
                double salesPrice = rs.getDouble("salesprice_with_tax");
                double coverage = rs.getDouble("coverage_ratio");

                return new Carport(carportId, width, height, length, new Price(priceId, purchasePrice, salesPrice, coverage));
            } else {
                throw new DatabaseException("Failed to retrieve carport for given invoice id.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("DB: SQL SELECT error in getCarport() in InvoiceMapper. ", e.getMessage());
        }
    }
}




