package app.controllers;

import app.entities.Customer;
import app.entities.Offer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OfferMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;



public class OfferController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
            app.get("/offer/{offerId}", ctx -> getOffer(ctx, connectionPool));
            app.post("/offer/{offerId}/accept", ctx -> acceptOffer(ctx, connectionPool));
        }


    public static void getOffer(Context ctx, ConnectionPool connectionPool) {
        try {
            Customer currentUser = ctx.sessionAttribute("currentUser");
            currentUser.getCustomerId();

            Offer offer = OfferMapper.getOfferByCustomerId(currentUser.getCustomerId(), connectionPool);

            ctx.json(offer);
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid offer ID");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error retrieving offer: " + e.getMessage());
        }
    }

    public static void acceptOffer(Context ctx, ConnectionPool connectionPool) {
        try {
            int offerId = Integer.parseInt(ctx.pathParam("offerId"));
            OfferMapper.updateOfferStatus(offerId, "Godkend", connectionPool);
            ctx.redirect("/accept-offer-page.html");
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid offer ID");
        } catch (DatabaseException e) {
            ctx.status(500).result("Error updating offer status: " + e.getMessage());
        }
    }
}
