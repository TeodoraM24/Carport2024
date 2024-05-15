package app.persistence;

import app.entities.Customer;
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
        String sql = "SELECT customer.customer_id,invoice.date AS invoice_date, invoice.invoice_id\n" +
                "FROM customer\n" +
                "INNER JOIN customer_invoice ON customer.customer_id = customer_invoice.customer_id \n" +
                "INNER JOIN invoice ON customer_invoice.invoice_id = invoice.invoice_id \n" +
                "INNER JOIN customer_carport ON customer.customer_id = customer_carport.customer_id\n" +
                "INNER JOIN carport_parts_list ON customer_carport.carport_id = carport_parts_list.carport_id \n" +
                "INNER JOIN parts_list ON carport_parts_list.parts_list_id = parts_list.parts_list_id \n" +
                "WHERE customer.customer_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("invoice_date").toLocalDate();
                int invoiceId = rs.getInt("invoice_id");
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
    public static List<CustomerPartslist> getACustomersPartslist(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        List<CustomerPartslist> listOfCustomerPartlist = new ArrayList<>();
        String sql = "SELECT customer.customer_id,material.material_description, material.length, parts_list.amount,unit.unit_name, parts_list.instruction_description\n" +
                "FROM customer\n" +
                "INNER JOIN customer_parts_list ON customer.customer_id = customer_parts_list.customer_id\n" +
                "INNER JOIN  parts_list ON customer_parts_list.parts_list_id = parts_list.parts_list_id\n" +
                "INNER JOIN material ON parts_list.parts_list_id = material.material_id\n" +
                "INNER JOIN unit ON parts_list.unit_id = unit.unit_id\n" +
                "WHERE customer.customer_id = ?";
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
//Get the specifik parlist ud fra Invoice_ID
//Display udfra enkel objekt fra Mapper i HTML th:object
// tilføj invoiceId I parameterne i metoden getACustomersPartslist og add det resterende.


    /**
     * Retrieves a customer from the database based on the provided customer ID.
     *
     * @param customerId   the ID of the customer to retrieve
     * @param connectionPool   the connection pool to use for the database connection
     * @return   the Customer object representing the retrieved customer
     * @throws DatabaseException   if there's an issue with the database operation
     */

//    public static Customer getCustomerById(int customerId, ConnectionPool connectionPool) throws DatabaseException {
//        Customer customer = new Customer(1); // Instantiate a new Customer object
//
//        String sql = "SELECT customer_id\n" +
//                "FROM customer\n" +
//                "WHERE customer_id = 1;";
//        //change the SQL when customer_id =!Null..
//        try (
//                Connection connection = connectionPool.getConnection();
//                PreparedStatement ps = connection.prepareStatement(sql)
//        ) {
//            ps.setInt(1, customerId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                customer.setCustomerId(rs.getInt("customer_id"));
//            }
//        } catch (SQLException e) {
//            throw new DatabaseException("Fejl i SQL!!", e.getMessage());
//        }
//        return customer;
//    }


}




