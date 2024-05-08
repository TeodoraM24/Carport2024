package app.controllers;

import app.entities.Customer;
import app.entities.Invoice;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.InvoiceMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class InvoiceController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> displayCurrentCustomersOrderHistory(ctx, connectionPool));
    }

    private static void displayCurrentCustomersOrderHistory(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer currentCustomer = ctx.sessionAttribute("currentCustomer");
        List<Invoice> listOfInvoices = InvoiceMapper.getACustomersInvoice(currentCustomer.getCustomerId(), connectionPool);
        ctx.attribute("listOfInvoices", listOfInvoices);
        ctx.render("templates/customer-own-order-page-html");
    }
}
