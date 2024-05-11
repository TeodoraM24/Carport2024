package app.controllers;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;
import app.persistence.CustomerRequestMapper;

import java.util.List;

public class CustomerRequestController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/customer-request-status", ctx -> getAllCustomerRequests(ctx, connectionPool));
    }


    public static void getAllCustomerRequests(Context ctx, ConnectionPool connectionPool) {
        try {

            Customer currentUser = ctx.sessionAttribute("currentUser");

            List<CustomerRequest> customerRequests = CustomerRequestMapper.getAllCustomerRequests(currentUser.getCustomerId(), connectionPool);

            ctx.json(customerRequests);
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving customer requests: " + e.getMessage());
        }
    }
}
