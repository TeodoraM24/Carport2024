package app.controllers;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.CustomerRequestMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Collections;
import java.util.List;

public class AdminCustomerRequestController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/admininquiries", ctx -> displayCustomerRequests(ctx, connectionPool));
        app.post("/admininquiries", ctx -> displayCustomerRequests(ctx, connectionPool));
    }

    private static void displayCustomerRequests(Context ctx, ConnectionPool connectionPool) {
        List<CustomerRequest> customerRequestList;

        try {
            customerRequestList = CustomerRequestMapper.getAllCustomerRequest(connectionPool);

            ctx.attribute("customerRequests", customerRequestList);

            ctx.render("admininquiries.html");
        } catch (DatabaseException e) {
            ctx.result("Der skete en fejl mens programmet hentede kundeforespørgsler");
        }
    }
}