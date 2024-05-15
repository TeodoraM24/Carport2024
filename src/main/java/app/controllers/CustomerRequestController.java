package app.controllers;

import app.entities.Customer;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.CustomerRequestMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
        app.get("/carport-index", ctx -> ctx.render("carport-index.html"));
        app.get("/carport-form", ctx -> ctx.render("carport-form.html"));
        app.get("/customer-info-page", ctx -> ctx.render("customer-info-page.html"));
        app.get("/login-page", ctx -> ctx.render("login-page.html"));
        app.post("/carport-offer-sent", ctx -> ctx.render("carport-offer-sent.html"));
        app.post("/make-customer-request", ctx -> makeCustomerRequest(ctx, connectionPool));
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
            ctx.redirect("/login-page.html");
            ctx.attribute("message", "Du skal logge ind før du kan bestille en forespørgsel");
            return;
        }

        if (currentUser.isHaveRequest()) {
            ctx.redirect("/customer-info-page.html");
            ctx.attribute("message", "Du har allerede bestilt en forespørgsel, vent venligst på vi vender tilbage");
            return;
        }


        try {
            CustomerRequestMapper.makeCustomerRequest(ctx, connectionPool);
            currentUser.setHaveRequest(true);
            ctx.redirect("/carport-offer-sent.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("carport-form.html");
        }
    }

    public static List<CustomerRequest> getAllCustomerRequestWithCustomer(ConnectionPool connectionPool) throws DatabaseException {

        List<CustomerRequest> customerRequestsWithCustomer = new ArrayList<>();

        String sql = "SELECT cr.customer_request_id, cr.length, cr.width, cr.height, cr.date, cr.status, cr.tile_type, c.first_name, c.last_name, c.customer_id " +
                "FROM customer_request cr " +
                "JOIN customer c ON cr.customer_id = c.customer_id";


        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int customerRequestId = rs.getInt("customer_request_id");
                int height = rs.getInt("height");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                LocalDate date = rs.getDate("date").toLocalDate();
                String status = rs.getString("status");
                String tileType = rs.getString("tile_type");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int customerId = rs.getInt("customer_id");

                Customer customer = new Customer(firstName, lastName, customerId);

                customerRequestsWithCustomer.add(new CustomerRequest(customerRequestId, height, width, length, tileType, date, status, customer));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl under indhentelse af kunde forespørgsler", e.getMessage());
        }
        System.out.println("Customer Requests with Customers: " + customerRequestsWithCustomer);
        return customerRequestsWithCustomer;
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