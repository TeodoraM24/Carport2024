package app.controllers.customer;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.customer.CustomerRequestMapper;
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
        app.get("/carport-form", ctx -> ctx.render("customer/carport-form.html"));
        app.get("/index", ctx -> ctx.render("customer/carport-standard.html"));
        app.get("/customer-request-status", ctx -> getAllCustomerRequests(ctx, connectionPool));
        app.get("/carport-index", ctx -> ctx.render("customer/carport-index.html"));
        app.get("/customer-info-page", ctx -> ctx.render("customer/customer-info-page.html"));
        app.post("/carport-offer-sent", ctx -> ctx.render("customer/carport-offer-sent.html"));
        app.post("/make-customer-request", ctx -> makeCustomerRequest(ctx, connectionPool));
        //app.get("showRequestStatus", ctx-> ctx.redirect("customer/customer-request-status.html"));
        app.get("showRequest", ctx -> getAllCustomerRequests(ctx,connectionPool));
    }

    public static void getAllCustomerRequests(Context ctx, ConnectionPool connectionPool) throws DatabaseException {


        Customer currentUser = ctx.sessionAttribute("currentUser");
        currentUser.getCustomerId();


        List<CustomerRequest> customerRequests = CustomerRequestMapper.getAllCustomerRequests(currentUser.getCustomer_request_id(), connectionPool);
            ctx.attribute("customerRequests", customerRequests);

            ctx.render("customer/customer-request-status.html");

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
            ctx.render("login/login-page.html");
            ctx.attribute("message", "Du skal logge ind før du kan bestille en forespørgsel");
            return;
        }

        try {
            LocalDate date = LocalDate.now();
            int height = Integer.parseInt(ctx.formParam("height"));
            int width = Integer.parseInt(ctx.formParam("width"));
            int length = Integer.parseInt(ctx.formParam("length"));

            int customerRequestId = CustomerRequestMapper.makeCustomerRequest(currentUser, height, width, length, date, connectionPool);
            currentUser.setCustomer_request_id(customerRequestId);

            ctx.render("customer/carport-offer-sent.html");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("customer/carport-form.html");
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
            ctx.render("customer/carport-offer-sent.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("customer/carport-form.html");
        }
    }
}
