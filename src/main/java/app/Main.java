package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.OfferController;
import app.entities.Offer;
import app.persistence.ConnectionPool;
import app.persistence.OfferMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;

import java.util.Map;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport_test";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Add routes and controllers
        OfferController.addRoutes(app, ConnectionPool.getInstance());

        app.get("/", ctx -> {
            // Retrieve offer from the database
            Offer offer = OfferMapper.getOfferByCustomerId(1, connectionPool);

            // Create a map to hold model attributes
            Map<String, Object> model = new HashMap<>();
            model.put("offer", offer);

            // Pass offer to the Thymeleaf template
            ctx.render("accept-or-deny-offer-customer", model);
        });
    }
}
