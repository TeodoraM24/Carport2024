package app.controllers;

import app.entities.Customer;
import app.entities.CustomerPartslist;
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
        //app.post("/customerinfo", ctx -> customerInfoPage(ctx, connectionPool));
        app.get("/orderoverview", ctx -> displayCustomerOwnOrderPage(ctx, connectionPool));
        app.post("/vieworder", ctx -> displayCustomerOwnOrderPage(ctx, connectionPool));

    }
    /**
     * Displays the customerInfo page for the current customer
     * Retrieves all necessary data from the database
     *
     * @param ctx            Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve materials data from the database
     * @throws DatabaseException Throws an exception if there is a failure in the database connection or SQL query
     */

//    private static void customerInfoPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException{
//        Customer customer = ctx.sessionAttribute("currentCustomer");
//        ctx.attribute("currentCustomerId", customer.getCustomerId());
//
//    }

    /**
     * Displays the order history page for the current customer
     * Retrieves all necessary data from the database
     *
     * @param ctx            Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve materials data from the database
     * @throws DatabaseException Throws an exception if there is a failure in the database connection or SQL query
     */

    private static void displayCustomerOwnOrderPage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        Customer customer = InvoiceMapper.getCustomerById(1, connectionPool);
        ctx.sessionAttribute("currentCustomer", customer);
        List<Invoice> listOfInvoices = null;
        listOfInvoices = InvoiceMapper.getACustomersInvoice(customer.getCustomerId(), connectionPool);
        ctx.attribute("listOfInvoices", listOfInvoices);

        ctx.render("customer-own-order-frontpage.html");
    }

    /**
     * Displays the order Partslist for the current customer
     * Retrieves all necessary data from the database
     *
     * @param ctx            Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve materials data from the database
     * @throws DatabaseException Throws an exception if there is a failure in the database connection or SQL query
     */

    private static void displayPartlist(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Customer customer = ctx.sessionAttribute("currentCustomerId");
        ctx.attribute("currentCustomerId", customer.getCustomerId());

        List<CustomerPartslist> listOfPartlists = null;
        listOfPartlists = InvoiceMapper.getACustomersPartslist(customer.getCustomerId(), connectionPool);
        ctx.attribute("listOfPartlists", listOfPartlists);

        ctx.render("customer-own-order-page.html");

    }
}