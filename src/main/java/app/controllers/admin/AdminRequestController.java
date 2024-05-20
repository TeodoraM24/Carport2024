package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.admin.AdminRequestMapper;
import app.persistence.customer.CustomerRequestMapper;
import app.services.calculator.PartsListCalculator;
import app.services.calculator.PriceCalculator;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AdminRequestController {
    private static List<PartsListItem> partsListItems;
    private static Price priceOffer;
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/admininquiries", ctx -> displayCustomerRequests(ctx, connectionPool));

        app.get("/chooseCustomer", ctx -> {
            setSessionCurrentRequest(ctx, connectionPool);
            ctx.redirect("/showRequestForCustomer");
        });

        app.get("/showRequestForCustomer", ctx -> {
            CustomerRequest customerRequest = getCustomerRequest(getSessionCurrentRequestId(ctx), connectionPool);
            displayChosenCustomerRequestPage(customerRequest, ctx);
        });

        app.get("/returnToAllRequests", ctx -> {
            removeSessionCurrentCustomer(ctx);
            ctx.redirect("/admininquiries");
        });

        app.get("/cancelRequestChanges", ctx -> ctx.redirect("/showRequestForCustomer"));

        app.post("/saveRequestChanges", ctx -> {
            saveRequestChanges(ctx, connectionPool);
            ctx.redirect("/showRequestForCustomer");
        });

        app.post("/calculateRequest", ctx -> {
            partsListItems = calculatePartsListForChosenCustomer(ctx, connectionPool);
            priceOffer = calculatePriceOfferForChosenCustomer(partsListItems);
            displayCalculateOfferPage(ctx, partsListItems, priceOffer);
        });

        app.post("/saveChanges", ctx -> {
            savePartsListChanges(ctx);
            priceOffer = calculateNewPriceOffer(ctx);
            displayCalculateOfferPage(ctx, partsListItems, priceOffer);
        });

        app.post("/showDescription", ctx -> {
            CustomerRequest customerRequest = getCustomerRequest(getSessionCurrentRequestId(ctx), connectionPool);
            displayOfferDescriptionPage(customerRequest, ctx);
        });

        app.get("/returnToCalculateRequest", ctx -> displayCalculateOfferPage(ctx, partsListItems, priceOffer));
    }

    private static void removeSessionCurrentCustomer(Context ctx) {
        ctx.req().removeAttribute("currentCustomerId");
        ctx.req().removeAttribute("currentCustomerName");
        ctx.req().removeAttribute("currentCustomerRequestId");
    }

    private static void setSessionCurrentRequest(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int customerId = Integer.parseInt(ctx.queryParam("customerId"));
        ctx.sessionAttribute("currentCustomerId", customerId);

        String customerName = ctx.queryParam("customerName");
        ctx.sessionAttribute("currentCustomerName", customerName);

        int customerRequestId = getCustomerRequestId(customerId, connectionPool);
        ctx.sessionAttribute("currentCustomerRequestId", customerRequestId);
    }

    public static int getSessionCurrentRequestId(Context ctx) {
        return ctx.sessionAttribute("currentCustomerRequestId");
    }

    public static int getSessionCurrentId(Context ctx) {
        return ctx.sessionAttribute("currentCustomerId");
    }

    public static List<PartsListItem> getAllPartsListItems(Context ctx) {
        List<PartsListItem> partsListItemsToSave = new ArrayList<>();
        int listSize = Integer.parseInt(ctx.formParam("list-size"));

        for (int i = 0; i < listSize; i++) {
            String description = ctx.formParam("partsListItems["+ i +"].material.description");
            String instruction = ctx.formParam("partsListItems["+ i +"].instruction");
            int length = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].material.length"));
            int width = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].material.width"));
            int height = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].material.height"));
            int amount = Integer.parseInt(ctx.formParam("partsListItems["+ i +"].amount"));
            String unit = ctx.formParam("partsListItems["+ i +"].unit");
            double totalPrice = Double.parseDouble(ctx.formParam("partsListItems["+ i +"].totalprice"));
            partsListItemsToSave.add(i, new PartsListItem(new Material(description, height, width, length, totalPrice/amount), amount, unit, instruction, totalPrice));
        }

        return partsListItemsToSave;
    }

    private static void displayCustomerRequests(Context ctx, ConnectionPool connectionPool) {
        List<CustomerRequest> customerRequestList;

        try {
            customerRequestList = CustomerRequestMapper.getAllCustomerRequest(connectionPool);
            ctx.attribute("customerRequests", customerRequestList);

            ctx.render("admin/admininquiries.html");
        } catch (DatabaseException e) {
            ctx.result("Der skete en fejl mens programmet hentede kundeforespørgsler");
        }
    }

    private static Price calculateNewPriceOffer(Context ctx) {
        String price = (ctx.formParam("new-price"));
        double newPrice = (!price.isEmpty()) ? Double.parseDouble(price) : priceOffer.getSalesPrice();

        if (newPrice == priceOffer.getSalesPrice()) {
            return calculatePriceOfferForChosenCustomer(partsListItems);
        } else {
            BigDecimal priceDifference = new BigDecimal(newPrice - priceOffer.getSalesPrice()).setScale(2, RoundingMode.HALF_UP);
            ctx.attribute("priceDifference", priceDifference.doubleValue());
            return calculatePriceOfferForChosenCustomer(partsListItems, newPrice);
        }
    }

    private static List<PartsListItem> calculatePartsListForChosenCustomer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int customerRequestId = Integer.parseInt(ctx.formParam("customer-request-id"));
        CustomerRequest customerRequest = getCustomerRequest(customerRequestId, connectionPool);
        Material post = new Material("97x97 mm. trykimp. Stolpe", 97, 97);
        Material beam = new Material("45x195 mm. spærtræ ubh.", 195, 45);
        Material rafter = new Material("45x195 mm. spærtræ ubh.", 195, 45);

        PartsListCalculator partsListCalculator = new PartsListCalculator(customerRequest.getRequestLength(), customerRequest.getRequestWidth(), customerRequest.getRequestHeight(), post, beam, rafter);
        partsListCalculator.calcCarport();

        return partsListCalculator.getPartsListItems();
    }

    private static void savePartsListChanges(Context ctx) {
        partsListItems = getAllPartsListItems(ctx);
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

        AdminRequestMapper.updateCustomerRequest(customerRequestId, requestLength, requestWidth, requestHeight, connectionPool);
    }

    private static int getCustomerRequestId(int customerId, ConnectionPool connectionPool) throws DatabaseException {
        return AdminRequestMapper.getCustomerRequestId(customerId, connectionPool);
    }

    private static CustomerRequest getCustomerRequest(int currentCustomerRequestId, ConnectionPool connectionPool) throws DatabaseException {
        return AdminRequestMapper.getCustomerRequest(currentCustomerRequestId, connectionPool);
    }

    private static void displayOfferDescriptionPage(CustomerRequest customerRequest, Context ctx) {
        ctx.attribute("requestId", getSessionCurrentRequestId(ctx));
        ctx.attribute("carportWidth", customerRequest.getRequestWidth());
        ctx.attribute("carportLength", customerRequest.getRequestLength());
        ctx.attribute("carportHeight", customerRequest.getRequestHeight());

        String rafterDescription = "Spær med rejsning";
        String supportBeamDescription = "Spærtræ 45x195 mm.";
        String tileType = "Plasttrapeztag";
        ctx.attribute("rafterDescription", rafterDescription);
        ctx.attribute("beamDescription", supportBeamDescription);
        ctx.attribute("tileType", tileType);
        ctx.attribute("offerPrice", priceOffer.getSalesPrice());

        ctx.render("admin/offer-information-page.html");
    }

    private static void displayCalculateOfferPage(Context ctx, List<PartsListItem> partsListItems, Price priceOffer) {
        String customerName = ctx.sessionAttribute("currentCustomerName");
        ctx.attribute("customerName", customerName);
        ctx.attribute("priceOffer", priceOffer);
        ctx.attribute("partsListItems", partsListItems);

        ctx.render("admin/calculate-offer.html");
    }

    private static void displayChosenCustomerRequestPage(CustomerRequest customerRequest, Context ctx) {
        String customerName = ctx.sessionAttribute("currentCustomerName");
        ctx.attribute("customerName", customerName);
        ctx.attribute("chosenCustomerRequest", customerRequest);

        ctx.render("admin/process-customer-request.html");
    }
}