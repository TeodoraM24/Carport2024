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
        app.get("/", ctx -> displayCurrentCustomersOrderHistory(ctx, connectionPool));
    }

    private static void displayCurrentCustomersOrderHistory(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        /*Customer currentCustomer = ctx.sessionAttribute("currentCustomer");*/
        Customer currentCustomer = CustomerMapper.logInd("jon@blabla.com","1234", connectionPool);
        if (currentCustomer != null) {
            List<Invoice> listOfInvoices = InvoiceMapper.getACustomersInvoice(currentCustomer.getCustomerId(), connectionPool);
            ctx.attribute("listOfInvoices", listOfInvoices);
            ctx.render("templates/customer-own-order-page-html");
        } else {
            ctx.status(404).result("Current customer not found");
            //ctx.redirect("templates/customer-info-frontpage.html");
        }
    }

}
