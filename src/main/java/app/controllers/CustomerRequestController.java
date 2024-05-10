package app.controllers;

import app.entities.CustomerRequest;
import app.entities.Material;
import app.entities.PartsList;
import app.entities.PartsListItem;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerRequestMapper;
import app.services.MaterialsCalculator;
import app.services.PriceCalculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class CustomerRequestController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> { //change
            //When admin is choosing a customer get their id from that page and send it here
            int customerId = 1; //change
            int customerRequestId = getCustomerRequestId(customerId, connectionPool);
            CustomerRequest customerRequest = getCustomerRequest(customerRequestId, connectionPool);
            displayChosenCustomerRequestPage(customerRequest, ctx);
        });

        app.get("/returnToAllRequests", ctx -> ctx.render("index.html")); //change

        app.post("/saveRequestChanges", ctx -> {
            saveRequestChanges(ctx, connectionPool);
            ctx.redirect("/"); //change
        });

        app.get("/cancelRequestChanges", ctx -> ctx.redirect("/")); //change

        app.post("/calculateRequest", ctx -> {
            calculateOfferForChosenCustomer(ctx, connectionPool);
            displayCalculateOfferPage(ctx);
        });
    }

    private static void calculateOfferForChosenCustomer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        //get the customer request id
        int customerRequestId = Integer.parseInt(ctx.formParam("customer-request-id"));

        //int get whole request
        CustomerRequest customerRequest = getCustomerRequest(customerRequestId, connectionPool);

        //calculate materials from request based on length, width, height
        MaterialsCalculator materialsCalculator = new MaterialsCalculator(customerRequest.getRequestLength(), customerRequest.getRequestWidth(), customerRequest.getRequestHeight(), connectionPool);
        materialsCalculator.calcCarport();
        List<PartsListItem> partsListItems = materialsCalculator.getPartsListItems();

        //calculate the prices from request
        int totalPrice = 0;
        for (PartsListItem p : partsListItems) {
            Material m = p.getMaterial();
            totalPrice += m.getPrice();
        }

        PriceCalculator priceCalculator = new PriceCalculator();
        //calculate here

        //Create Parts list

    }

    private static void displayCalculateOfferPage(Context ctx) {
        //ctx.attributte on all materials in parts list
        //ctx.attributte on prices
        ctx.render("calculate-offer.html");
    }

    private static void saveRequestChanges(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int requestLength = Integer.parseInt(ctx.formParam("length"));
        int requestWidth = Integer.parseInt(ctx.formParam("width"));
        int requestHeight = Integer.parseInt(ctx.formParam("height"));

        int customerRequestId = Integer.parseInt(ctx.formParam("customer-request-id"));
        CustomerRequestMapper.updateCustomerRequest(customerRequestId, requestLength, requestWidth, requestHeight, connectionPool);
    }

    private static int getCustomerRequestId(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        return CustomerRequestMapper.getCustomerRequestId(customerId, connectionPool);
    }

    private static CustomerRequest getCustomerRequest(int customerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        return CustomerRequestMapper.getCustomerRequest(customerRequestId, connectionPool);
    }

    private static void displayChosenCustomerRequestPage(CustomerRequest customerRequest, Context ctx) {
        ctx.attribute("customerName", "Jon Andersen"); //Change
        ctx.attribute("chosenCustomerRequest", customerRequest);
        ctx.render("process-customer-request.html");
    }
}
