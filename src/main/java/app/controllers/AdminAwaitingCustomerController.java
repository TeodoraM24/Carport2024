package app.controllers;

import app.entities.CustomerRequest;
import app.entities.Offer;
import app.exceptions.DatabaseException;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;

public class AdminAwaitingCustomerController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/awaitingCustomers", ctx -> {
            List<Offer> allCustomerOffers = getAllCustomerOffers(connectionPool);
            displayAwaitingCustomer(allCustomerOffers, ctx);
        });

        app.get("/sendInvoice/{offerId}", ctx -> {
           createInvoice(ctx, connectionPool);
           ctx.redirect("/awaitingCustomers");
        });

        app.get("/deleteOffer/{offerId}",  ctx -> {
            deleteOfferAndRequest(ctx, connectionPool);
            ctx.redirect("/awaitingCustomers");
        });
    }

    private static void deleteOfferAndRequest(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int offerId = Integer.parseInt(ctx.pathParam("offerId"));
        Offer offer = OfferMapper.getOfferById(offerId, connectionPool);
        CustomerMapper.deleteOfferId(offerId, connectionPool);
        CustomerMapper.deleteRequestId(offer.getCustomerRequestId(), connectionPool);
        OfferMapper.deleteOffer(offer.getOfferId(), connectionPool);
        CustomerRequestMapper.deleteCustomerRequest(offer.getCustomerRequestId(), connectionPool);
    }

    private static void createInvoice(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int offerId = Integer.parseInt(ctx.pathParam("offerId"));
        Offer offer = OfferMapper.getOfferById(offerId, connectionPool);
        int customerId = CustomerMapper.getCustomerIdByOfferId(offerId, connectionPool);
        CustomerRequest customerRequest = AdminRequestMapper.getCustomerRequest(offer.getCustomerRequestId(), connectionPool);
        int carportId = PurchaseMapper.addCarport(customerRequest.getRequestWidth(), customerRequest.getRequestHeight(), customerRequest.getRequestLength(), offer.getPriceId(), connectionPool);
        LocalDate currentDate = LocalDate.now();
        int invoiceId = PurchaseMapper.addInvoice(offer.getPartsListId(), currentDate, carportId, connectionPool);
        PurchaseMapper.addInvoiceToCustomer(customerId, invoiceId, connectionPool);
        CustomerMapper.deleteOfferId(offerId, connectionPool);
        CustomerMapper.deleteRequestId(offer.getCustomerRequestId(), connectionPool);
        OfferMapper.deleteOffer(offer.getOfferId(), connectionPool);
        CustomerRequestMapper.deleteCustomerRequest(offer.getCustomerRequestId(), connectionPool);
    }

    private static List<Offer> getAllCustomerOffers(ConnectionPool connectionPool) throws DatabaseException {
        return OfferMapper.getAllCustomerOffers(connectionPool);
    }

    private static void displayAwaitingCustomer( List<Offer> allCustomerOffers, Context ctx) {
        ctx.attribute("awaitingOffers", allCustomerOffers);
        ctx.render("admin-awaiting-customer.html");
    }
}
