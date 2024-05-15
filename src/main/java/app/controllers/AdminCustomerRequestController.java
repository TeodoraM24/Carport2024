package app.controllers;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.CustomerRequestMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class AdminCustomerRequestController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/admininquiries", ctx -> displayCustomerRequests(ctx, connectionPool));
        app.post("/admininquiries", ctx -> displayCustomerRequests(ctx, connectionPool));
    }

    private static void displayCustomerRequests(Context ctx, ConnectionPool connectionPool) {

        List<CustomerRequest> customerRequestList = null;

        try {
            // Retrieve all customer requests
            customerRequestList = CustomerRequestMapper.getAllCustomerRequest(connectionPool);

            // Add the list of customer requests as an attribute to the context
            ctx.attribute("customerRequests", customerRequestList);

            // Render the HTML template
            ctx.render("customer-request-admin.html");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}

