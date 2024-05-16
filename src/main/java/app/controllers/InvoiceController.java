package app.controllers;

import app.entities.Customer;
import app.entities.InvoiceDetails;
import app.entities.Invoice;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.InvoiceMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class InvoiceController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        /*app.get("/", ctx -> {
            ctx.render("customer-info-frontpage.html");
        });*/

        app.get("backToInfoPage", ctx -> {
            ctx.redirect("customer-info-frontpage.html");
        });

        app.get("viewOrderHistory", ctx -> displayCustomerOrderHistory(ctx, connectionPool));

        app.get("viewInvoiceDetails/{invoiceid}", ctx -> {
            //get invoiceId
            int id = Integer.parseInt(ctx.pathParam("invoiceid"));
            System.out.println(id);
            displayInvoiceDetails(ctx, connectionPool);
        });
    }

    /***
     * Displays the order history of a specific customer.
     * Retrieves the order history for the specified customer and renders it to the customer-order-history-page HTML template.
     *
     * @param ctx The context object providing access to the web application's environment
     * @param connectionPool The ConnectionPool object used to establish a database connection
     * @throws DatabaseException Thrown if there's an error in retrieving the order history from the database
     */

    private static void displayCustomerOrderHistory(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        Customer customer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej", 2770, "customer");
        ctx.sessionAttribute("currentCustomer", customer);
        List<Invoice> listOfInvoices = null;
        listOfInvoices = InvoiceMapper.getCustomersOrderHistory(1, connectionPool);
        ctx.attribute("listOfInvoices", listOfInvoices);

        ctx.render("customer-order-history-page.html");
    }

    /***
     * Display the details of a specific invoice for a customer.
     * Retrieves the details of the specified invoice for the current customer and renders them to the customer-invoice-details-page HTML template.
     *
     * @param ctx The context object providing access to the web application's environment
     * @param connectionPool The ConnectionPool object used to establish a database connection
     * @throws DatabaseException Thrown if there's an error in retrieving the invoice details from the database
     */


    private static void displayInvoiceDetails(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer customer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej", 2770, "customer");
        ctx.sessionAttribute("currentCustomerId");
        ctx.attribute("currentCustomerId", customer.getCustomerId());

        //get invoiceId
        int id = Integer.parseInt(ctx.pathParam("invoiceid"));

        List<InvoiceDetails> listOfPartlists = null;
        listOfPartlists = InvoiceMapper.getCustomerInvoiceDetails(customer.getCustomerId(),id, connectionPool);
        ctx.attribute("listOfPartlists", listOfPartlists);

        ctx.render("customer-invoice-details-page.html");

    }
}