package app.controllers;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.CustomerRequestMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class reponsible for the handling of customer requests
 */

public class CustomerRequestController {

    /**
     * Add routes to Javalin
     *
     * @param app Instance of the Javalin application
     * @param connectionPool The connection to the database
     */

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/carport-form", ctx -> ctx.render("carport-form.html"));
        app.get("/index", ctx -> ctx.render("index.html"));
        app.get("/customer-request-status", ctx -> getAllCustomerRequests(ctx, connectionPool));
        app.get("/carport-index", ctx -> ctx.render("carport-index.html"));
        app.get("/customer-info-page", ctx -> ctx.render("customer-info-page.html"));
        app.post("/carport-offer-sent", ctx -> ctx.render("carport-offer-sent.html"));
        app.post("/make-customer-request", ctx -> makeCustomerRequest(ctx, connectionPool));
    }

    public static void getAllCustomerRequests(Context ctx, ConnectionPool connectionPool) {
        try {

            Customer currentUser = ctx.sessionAttribute("currentUser");
            currentUser.getCustomerId();


            List<CustomerRequest> customerRequests = CustomerRequestMapper.getAllCustomerRequests(currentUser.getCustomerId(), connectionPool);

            ctx.json(customerRequests);
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving customer requests: " + e.getMessage());
        }
    }

    /**
     * Handle the process of making customer request
     *
     * @param ctx The Javalin HTTP context
     * @param connectionPool The connection to the database
     *
     * Makes a check if the customer is logged in and if they're checking if they already have a request.
     */

    public static void makeCustomerRequest(Context ctx, ConnectionPool connectionPool) {
        Customer currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.render("login-page.html");
            ctx.attribute("message", "Du skal logge ind før du kan bestille en forespørgsel");
            return;
        }

        try {
            LocalDate date = LocalDate.now();
            int height = Integer.parseInt(ctx.formParam("height"));
            int width = Integer.parseInt(ctx.formParam("width"));
            int length = Integer.parseInt(ctx.formParam("length"));

            CustomerRequestMapper.makeCustomerRequest(currentUser, height, width, length, date, connectionPool);

            ctx.render("carport-offer-sent.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("carport-form.html");
        }
    }

    /**
     * Handle the process of deleting customer request
     *
     * @param ctx The Javalin HTTP context
     * @param connectionPool The connection to the database
     */

    public static void deleteCustomerRequest(Context ctx, ConnectionPool connectionPool) {
        int customerRequestId = Integer.parseInt(ctx.formParam("customerRequestId"));

        try {
            CustomerRequestMapper.deleteCustomerRequest(customerRequestId, connectionPool);
            List<CustomerRequest> customerRequestList = CustomerRequestMapper.getAllCustomerRequest(connectionPool);
            ctx.attribute("customerRequestList", customerRequestList);
            ctx.render("carport-offer-sent.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("carport-form.html");
        }
    }
}
