package app.controllers;

import app.entities.Customer;
import app.entities.Invoice;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.InvoiceMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class InvoiceController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("invoice", ctx -> displayCurrentCustomersOrderHistory(ctx, connectionPool));
        app.post("invoice", ctx -> displayCurrentCustomersOrderHistory(ctx, connectionPool));

    }

    /**
     * Displays the order history page for the current customer
     * Retrieves all necessary data from the database
     *
     * @param ctx            Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve materials data from the database
     * @throws DatabaseException Throws an exception if there is a failure in the database connection or SQL query
     */

    private static void displayCurrentCustomersOrderHistory(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer currentCustomer = ctx.sessionAttribute("currentCustomer");
        ctx.attribute("currentCustomerId", currentCustomer.getCustomerId());
        int aCustomer = 0;
        List<Invoice> listOfInvoices = InvoiceMapper.getACustomersInvoice(aCustomer, connectionPool);
        ctx.attribute("listOfInvoices", listOfInvoices);
        ctx.render("customer-own-order-frontpage.html");
    }

}