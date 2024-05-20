package app.controllers.customer;

import app.controllers.SvgController;
import app.entities.Carport;
import app.entities.Customer;
import app.entities.InvoiceDetails;
import app.entities.Invoice;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.customer.InvoiceMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class InvoiceController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("viewOrderHistory", ctx -> displayCustomerOrderHistory(ctx, connectionPool));

        app.get("viewInvoiceDetails/{invoiceid}", ctx -> {
            displayInvoiceDetails(ctx, connectionPool);
        });
        app.get("backToOrderHistory", ctx -> displayCustomerOrderHistory(ctx, connectionPool));
        app.get("backToInfoPage", ctx -> ctx.render("customer/customer-info-frontpage.html"));

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

        Customer customer = ctx.sessionAttribute("currentUser");
        ctx.sessionAttribute("currentCustomer", customer);
        List<Invoice> listOfInvoices = InvoiceMapper.getCustomersOrderHistory(customer.getCustomerId(), connectionPool);

        ctx.attribute("listOfInvoices", listOfInvoices);
        for (Invoice i: listOfInvoices)
        {
            System.out.println(i.getInvoiceID());
        }

        ctx.render("customer/customer-order-history-page.html");
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
        Customer customer = ctx.sessionAttribute("currentUser");
        ctx.sessionAttribute("currentCustomerId");
        ctx.attribute("currentCustomerId", customer.getCustomerId());

        int invoiceId = Integer.parseInt(ctx.pathParam("invoiceid"));


        List<InvoiceDetails> listOfPartlists = InvoiceMapper.getCustomerInvoiceDetails(customer.getCustomerId(), invoiceId, connectionPool);
        ctx.attribute("listOfPartlists", listOfPartlists);

        Carport carport = InvoiceMapper.getCarport(invoiceId, connectionPool);
        SvgController.displaySvg(carport, ctx);

        ctx.render("customer/customer-invoice-details-page.html");

    }
}