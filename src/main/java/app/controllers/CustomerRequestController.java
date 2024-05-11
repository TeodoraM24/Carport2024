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

public class CustomerRequestController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/carport-index", ctx -> ctx.render("carport-index.html"));
        app.get("/carport-form", ctx -> ctx.render("carport-form.html"));
        app.get("/customer-info-page", ctx -> ctx.render("customer-info-page.html"));
        app.get("/login-page", ctx -> ctx.render("login-page.html"));
        app.post("/carport-offer-sent", ctx -> ctx.render("carport-offer-sent.html"));
        app.post("/make-customer-request", ctx -> makeCustomerRequest(ctx, connectionPool));
    }

    public static void makeCustomerRequest(Context ctx, ConnectionPool connectionPool) {
        Customer currentUser = ctx.sessionAttribute("currentUser");
        if (currentUser == null) {
            ctx.render("/login-page.html");
            ctx.attribute("message", "Du skal logge ind før du kan bestille en forespørgsel");
            return;
        }

        if (currentUser.isHaveRequest()){
            ctx.redirect("/customer-info-page.html");
            ctx.attribute("message", "Du har allerede bestilt en forespørgsel, vent venligst på vi vender tilbage");
            return;
        }

        LocalDate date = LocalDate.now();
        int height = Integer.parseInt(ctx.formParam("carport-height"));
        int width = Integer.parseInt(ctx.formParam("carport-width"));
        int length = Integer.parseInt(ctx.formParam("carport-length"));

        try {
            CustomerRequestMapper.makeCustomerRequest(length, width, height, date, connectionPool);
            currentUser.setHaveRequest(true);
            ctx.redirect("carport-offer-sent.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("carport-form.html");
        }
    }
/*
    public static void updateCustomerRequest(Context ctx, ConnectionPool connectionPool) {
        try{
        LocalDate date = LocalDate.now();
        int height = Integer.parseInt(ctx.formParam("height"));
        int width = Integer.parseInt(ctx.formParam("width"));
        int length = Integer.parseInt(ctx.formParam("length"));
        String status = ctx.formParam("status");

        CustomerRequestMapper.updateCustomerRequest(length, width, height, date, status, connectionPool);
        List<CustomerRequest> customerRequestList = CustomerRequestMapper.getAllCustomerRequest(connectionPool);
        ctx.attribute("customerRequestList", customerRequestList);
        ctx.render("carport-form.html");
    } catch (DatabaseException e) {
        ctx.attribute("message", e.getMessage());
        ctx.render("carport-form.html");
        }
    }*/

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