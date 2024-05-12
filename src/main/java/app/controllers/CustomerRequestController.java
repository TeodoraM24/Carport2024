package app.controllers;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerRequestMapper;
import app.services.PartsListCalculator;
import app.services.PriceCalculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class CustomerRequestController {
    private static List<PartsListItem> partsListItems;
    private static Price priceOffer;
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
            partsListItems = calculatePartsListForChosenCustomer(ctx, connectionPool);
            priceOffer = calculatePriceOfferForChosenCustomer(partsListItems);
            displayCalculateOfferPage(ctx, partsListItems, priceOffer);
        });

        app.post("/saveChanges", ctx -> {
            savePartsListChanges(ctx);
            Price newPriceOffer = calculateNewPriceOffer(ctx);
            displayCalculateOfferPage(ctx, partsListItems, newPriceOffer);
        });
    }

    private static void savePartsListChanges(Context ctx) {
        List<PartsListItem> changedPartsListItems = new ArrayList<>();
        for (int i = 0; i < partsListItems.size(); i++) {
            String description = ctx.formParam("partsListItems["+ i +"].material.description");
            String instruction = ctx.formParam("partsListItems["+ i +"].instruction");
            int length = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].material.length"));
            int width = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].material.width"));
            int height = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].material.height"));
            int amount = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].amount"));
            String unit = ctx.formParam("partsListItems["+ i +"].unit");
            double totalPrice = Double.parseDouble(ctx.formParam("partsListItems["+ i +"].totalprice"));
            changedPartsListItems.add(i, new PartsListItem(new Material(description, height, width, length, totalPrice/amount), amount, unit, instruction, totalPrice));
        }
        partsListItems = changedPartsListItems;
    }

    private static Price calculateNewPriceOffer(Context ctx) {
        String price = (ctx.formParam("new-price"));
        double newPrice = (!price.isEmpty()) ? Double.parseDouble(price) : priceOffer.getSalesPrice();

        if (newPrice == priceOffer.getSalesPrice()) {
            return calculatePriceOfferForChosenCustomer(partsListItems);
        } else {
            double priceDifference = newPrice - priceOffer.getSalesPrice();
            ctx.attribute("priceDifference", priceDifference);
            return calculatePriceOfferForChosenCustomer(partsListItems, newPrice);
        }
    }

    private static List<PartsListItem> calculatePartsListForChosenCustomer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int customerRequestId = Integer.parseInt(ctx.formParam("customer-request-id"));

        CustomerRequest customerRequest = getCustomerRequest(customerRequestId, connectionPool);
        PartsListCalculator partsListCalculator = new PartsListCalculator(customerRequest.getRequestLength(), customerRequest.getRequestWidth(), customerRequest.getRequestHeight());
        partsListCalculator.calcCarport();

        return partsListCalculator.getPartsListItems();
    }

    private static Price calculatePriceOfferForChosenCustomer(List<PartsListItem> partsListItems) {
        PriceCalculator priceCalculator = new PriceCalculator(partsListItems);

        return new Price(priceCalculator.calcPurchasePrice(), priceCalculator.getSalesPrice(), priceCalculator.calcPriceWithoutTax(), priceCalculator.calcCoverage());
    }
    private static Price calculatePriceOfferForChosenCustomer(List<PartsListItem> partsListItems, double salesPrice) {
        PriceCalculator priceCalculator = new PriceCalculator(partsListItems, salesPrice);

        return new Price(priceCalculator.calcPurchasePrice(), priceCalculator.getSalesPrice(), priceCalculator.calcPriceWithoutTax(), priceCalculator.calcCoverage());
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

    private static void displayCalculateOfferPage(Context ctx, List<PartsListItem> partsListItems, Price priceOffer) {
        ctx.attribute("customerName", "Jon Andersen"); //Change
        ctx.attribute("priceOffer", priceOffer);
        ctx.attribute("partsListItems", partsListItems);

        ctx.render("calculate-offer.html");
    }

    private static void displayChosenCustomerRequestPage(CustomerRequest customerRequest, Context ctx) {
        ctx.attribute("customerName", "Jon Andersen"); //Change
        ctx.attribute("chosenCustomerRequest", customerRequest);

        ctx.render("process-customer-request.html");
    }
}
