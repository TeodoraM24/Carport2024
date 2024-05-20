package app.controllers.admin;

import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.*;
import app.persistence.admin.*;
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

    private static void savePartsList(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Price price = getPriceOffer(ctx);
        int priceId = AdminOfferMapper.addPrice(price.getPurchasePrice(), price.getSalesPrice(), price.getCoverage(), connectionPool);
        int partsListId = PartsListMapper.addPartsList(priceId, connectionPool);
        List<PartsListItem> partsListItemsToBeSaved = AdminRequestController.getAllPartsListItems(ctx);

        for (PartsListItem partsListItem: partsListItemsToBeSaved) {
            int partsListItemId = getPartsListItemId(partsListItem, connectionPool);
            PartsListMapper.addPartsListItem(partsListId, partsListItemId, connectionPool);
        }

        partsList = new PartsList(partsListId, new Price(priceId, price.getPurchasePrice(), price.getSalesPrice(), price.getCoverage()));
    }

    private static int getPartsListItemId(PartsListItem partsListItem, ConnectionPool connectionPool) throws DatabaseException {
        Material material = partsListItem.getMaterial();

        int materialId = MaterialMapper.addMaterial(material.getDescription(), material.getHeight(), material.getWidth(), material.getLength(), material.getPrice(), connectionPool);
        if (materialId == -1) {
            materialId = MaterialMapper.getMaterialIdByData(material.getDescription(), material.getHeight(), material.getWidth(), material.getLength(), material.getPrice(), connectionPool);
        }

        return PartsListItemMapper.addPartsListItem(materialId, partsListItem.getAmount(), partsListItem.getInstruction(), partsListItem.getUnit(), partsListItem.getTotalPrice(), connectionPool);
    }

    private static Price getPriceOffer(Context ctx) {
        double purchasePrice = Double.parseDouble(ctx.formParam("purchase-price"));
        double coverage = Double.parseDouble(ctx.formParam("coverage"));
        double priceWithoutTax = Double.parseDouble(ctx.formParam("price-without-tax"));
        double salesPrice = Double.parseDouble(ctx.formParam("sales-price"));

        return new Price(purchasePrice, salesPrice, priceWithoutTax, coverage);
    }

    private static void createOffer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int partsListId = partsList.getPartsListId();
        int priceId = partsList.getPrice().getPriceId();
        int customerId = AdminRequestController.getSessionCurrentId(ctx);
        int customerRequestId = AdminRequestController.getSessionCurrentRequestId(ctx);
        String rafterDescription = "Spær med rejsning";
        String supportBeamDescription = "Spærtræ 45x195 mm.";
        String tileType = "Plasttrapeztag";
        LocalDate currentDate = LocalDate.now();

        int offerId = AdminOfferMapper.addOffer(rafterDescription, supportBeamDescription, tileType, currentDate ,partsListId, priceId, customerRequestId, connectionPool);
        OfferMapper.updateCustomerOffer(offerId, customerId, connectionPool);
    }

    private static void setCustomerRequestStatus(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int customerRequestId = AdminRequestController.getSessionCurrentRequestId(ctx);
        String status = "Klar";

        AdminRequestMapper.updateCustomerRequestStatus(customerRequestId, status, connectionPool);
    }

    private static void displayOfferSent(Context ctx) {
        ctx.render("admin/offer-sent-result-page.html");
    }
}
