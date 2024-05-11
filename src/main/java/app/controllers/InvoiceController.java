package app.controllers;

import app.entities.Customer;
import app.entities.CustomerPartslist;
import app.entities.Invoice;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.InvoiceMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class InvoiceController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orderoverview", ctx -> displayCustomerOwnOrderPage(ctx, connectionPool));
        app.post("/vieworder", ctx -> displayCustomerOwnOrderPage(ctx, connectionPool));

    }

    /**
     * Displays the order history page for the current customer
     * Retrieves all necessary data from the database
     *
     * @param ctx            Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve materials data from the database
     * @throws DatabaseException Throws an exception if there is a failure in the database connection or SQL query
     */

    private static void displayCustomerOwnOrderPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer customer = new Customer(1, "rebecca@yes.dk", "yes", 1, "Rebecca", "Sørensen", "Østerbro", 2100, "customer");
        customer = ctx.sessionAttribute("currentCustomer");
        ctx.attribute("currentCustomerId", customer.getCustomerId());
        List<Invoice> listOfInvoices = null;
        listOfInvoices = InvoiceMapper.getACustomersInvoice(customer.getCustomerId(),connectionPool);
        ctx.attribute("listOfInvoices", listOfInvoices);

        ctx.render("customer-own-order-frontpage.html");
    }
    private static void displayPartlist(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer customer2 = new Customer(1, "rebecca@yes.dk", "yes", 1, "Rebecca", "Sørensen", "Østerbro", 2100, "customer");
        customer2 = ctx.sessionAttribute("currentCustomerId");
        List<CustomerPartslist> listOfPartlists = null;
        listOfPartlists = InvoiceMapper.getACustomersPartslist(customer2.getCustomerId(), connectionPool);
        ctx.attribute("listOfPartlists", listOfPartlists);

        ctx.render("customer-own-order-page.html");

    }

}