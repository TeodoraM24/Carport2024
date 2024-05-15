package app.controllers;

import app.entities.Material;
import app.entities.PartsList;
import app.entities.PartsListItem;
import app.entities.Price;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;

public class AdminOfferController {
    private static PartsList partsList;
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/sendOffer", ctx -> {
            savePartsList(ctx, connectionPool);
            createOffer(ctx, connectionPool);
            setCustomerRequestStatus(ctx, connectionPool);
            displayOfferSent(ctx);
        });
    }

    private static void setCustomerRequestStatus(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int customerRequestId = ctx.sessionAttribute("currentCustomerRequestId");
        String status = "Klar";

        AdminRequestMapper.updateCustomerRequestStatus(customerRequestId, status, connectionPool);
    }

    private static void savePartsList(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Price price = getPriceOffer(ctx);
        int priceId = AdminOfferMapper.addPrice(price.getPurchasePrice(), price.getSalesPrice(), price.getCoverage(), connectionPool);
        int partsListId = PartsListMapper.addPartsList(priceId, connectionPool);
        List<PartsListItem> partsListItemsToBeSaved = AdminRequestController.getAllPartsListItems(ctx);

        for (PartsListItem partsListItem: partsListItemsToBeSaved) {
            int partsListItemId = getPartsListItemId(partsListItem, connectionPool);
            PartsListMapper.addPartsListItem(partsListId, partsListItemId, connectionPool);
        }

        partsList = new PartsList(partsListId,priceId);
    }

    private static int getPartsListItemId(PartsListItem partsListItem, ConnectionPool connectionPool) throws DatabaseException {
        Material material = partsListItem.getMaterial();

        int materialId = MaterialMapper.addMaterial(material.getDescription(), material.getHeight(), material.getWidth(), material.getLength(), material.getPrice(), connectionPool);
        if (materialId == -1) {
            materialId = MaterialMapper.getMaterialIdByData(material.getDescription(), material.getHeight(), material.getWidth(), material.getLength(), material.getPrice(), connectionPool);
        }

        return PartsListItemMapper.addPartsListItem(materialId, partsListItem.getAmount(), partsListItem.getInstruction(), partsListItem.getUnit(), partsListItem.getTotalPrice(), connectionPool);
    }

    private static void createOffer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int partsListId = partsList.getPartsListId();
        int priceId = partsList.getPriceId();
        int customerRequestId = ctx.sessionAttribute("currentCustomerRequestId");
        String rafterDescription = "Spær med rejsning";
        String supportBeamDescription = "Spærtræ 45x195 mm.";
        String tileType = "Plasttrapeztag";
        LocalDate currentDate = LocalDate.now();

        AdminOfferMapper.addOffer(rafterDescription, supportBeamDescription, tileType, currentDate ,partsListId, priceId, customerRequestId, connectionPool);
    }

    private static Price getPriceOffer(Context ctx) {
        double purchasePrice = Double.parseDouble(ctx.formParam("purchase-price"));
        double coverage = Double.parseDouble(ctx.formParam("coverage"));
        double priceWithoutTax = Double.parseDouble(ctx.formParam("price-without-tax"));
        double salesPrice = Double.parseDouble(ctx.formParam("sales-price"));

        return new Price(purchasePrice, salesPrice, priceWithoutTax, coverage);
    }

    private static void displayOfferSent(Context ctx) {
        ctx.render("offer-sent-result-page.html");
    }
}
