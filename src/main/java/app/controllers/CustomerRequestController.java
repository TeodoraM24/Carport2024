package app.controllers;

import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.CustomerRequestMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;

public class CustomerRequestController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {

    }
    private static void addCustomerRequest(Context ctx, ConnectionPool connectionPool) {
        int length = Integer.parseInt(ctx.formParam("length"));
        int width = Integer.parseInt(ctx.formParam("width"));
        int height = Integer.parseInt(ctx.formParam("height"));
        LocalDate localDate = LocalDate.now();

        try {
            CustomerRequestMapper.makeCustomerRequest(length, width, height, localDate, connectionPool);
            List<CustomerRequest> customerRequestList = CustomerRequestMapper.getAllCustomerRequest(connectionPool);

            ctx.render("carport-offer-sent.html");
        } catch (DatabaseException e){
            ctx.attribute("message", "Please try again");
            ctx.render("carport-form.html");
        }
    }

}