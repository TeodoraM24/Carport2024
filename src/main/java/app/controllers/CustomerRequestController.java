package app.controllers;

import app.entities.CustomerRequest;
import app.entities.PartsListItem;
import app.entities.Price;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerRequestMapper;
import app.services.PartsListCalculator;
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
            List<PartsListItem> partsListItems = calculatePartsListForChosenCustomer(ctx, connectionPool);
            Price priceOffer = calculatePriceOfferForChosenCustomer(partsListItems);
            displayCalculateOfferPage(ctx, partsListItems, priceOffer);
        });
    }

    private static List<PartsListItem> calculatePartsListForChosenCustomer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        //get the customer request id
        int customerRequestId = Integer.parseInt(ctx.formParam("customer-request-id"));

        //int get whole request
        CustomerRequest customerRequest = getCustomerRequest(customerRequestId, connectionPool);

        //calculate materials from request based on length, width, height
        PartsListCalculator partsListCalculator = new PartsListCalculator(customerRequest.getRequestLength(), customerRequest.getRequestWidth(), customerRequest.getRequestHeight());
        partsListCalculator.calcCarport();
        return partsListCalculator.getPartsListItems();
    }

    private static Price calculatePriceOfferForChosenCustomer(List<PartsListItem> partsListItems) {
        PriceCalculator priceCalculator = new PriceCalculator(partsListItems);
        return new Price(priceCalculator.calcPurchasePrice(), priceCalculator.getSalesPrice(), priceCalculator.calcPriceWithoutTax(), priceCalculator.calcCoverage());
    }

    private static void displayCalculateOfferPage(Context ctx, List<PartsListItem> partsListItems, Price priceOffer) {
        ctx.attribute("customerName", "Jon Andersen"); //Change
        ctx.attribute("partsListItems", partsListItems);
        ctx.attribute("priceOffer", priceOffer);
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
