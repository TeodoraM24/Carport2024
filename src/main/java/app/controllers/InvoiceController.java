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
        app.get("/", ctx -> displayCustomerOwnOrderPage(ctx, connectionPool));

        app.get("vieworderallorder/{invoiceid}", ctx -> {
            //get invoiceId
            int id = Integer.parseInt(ctx.pathParam("invoiceid"));
            System.out.println(id);
            displayPartlist(ctx, connectionPool);
        });

        app.get("/", ctx -> displayCustomerInfoPage(ctx, connectionPool));
        //app.post("/vieworderallorder", ctx -> displayCustomerOwnOrderPage(ctx, connectionPool));
        //app.post("/viewaorder", ctx -> displayPartlist(ctx, connectionPool));

    }

    private static void displayCustomerInfoPage(Context ctx, ConnectionPool connectionPool) {



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

        Customer customer = new Customer(1, "jon@blabla.com", "1234", 12455, "Jon", "Andersen", "Campusvej", 2770, "customer" );
        ctx.sessionAttribute("currentCustomer", customer);
        List<Invoice> listOfInvoices = null;
        listOfInvoices = InvoiceMapper.getACustomersInvoice(1, connectionPool);
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

        //get invoiceId
        int id = Integer.parseInt(ctx.pathParam("invoiceid"));
        System.out.println(id);

        List<CustomerPartslist> listOfPartlists = null;
        listOfPartlists = InvoiceMapper.getACustomersPartslist(customer.getCustomerId(), connectionPool);
        ctx.attribute("listOfPartlists", listOfPartlists);

        ctx.render("customer-own-order-page.html");

    }
}