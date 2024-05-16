package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.AdminOfferController;
import app.controllers.AdminRequestController;
import app.controllers.CustomerController;
import app.controllers.SvgController;
import app.controllers.MaterialController;
import app.controllers.OfferController;
import app.entities.Offer;
import app.persistence.ConnectionPool;
import app.persistence.OfferMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import static app.controllers.SvgController.displaySvg;

import java.util.HashMap;

import java.util.Map;

public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "carport_test";

    public static void main(String[] args) {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        AdminRequestController.addRoutes(app, ConnectionPool.getInstance());
        AdminOfferController.addRoutes(app, ConnectionPool.getInstance());
        MaterialController.addRoutes(app, ConnectionPool.getInstance());
        CustomerController.addRoutes(app, ConnectionPool.getInstance());
        SvgController.addRoutes(app, ConnectionPool.getInstance());
        // Add routes and controllers
        OfferController.addRoutes(app, ConnectionPool.getInstance());

        app.get("/", ctx -> {
            // Retrieve offer from the database
            Offer offer = OfferMapper.getOfferByCustomerId(1, connectionPool);

        //app.get("/", ctx -> displaySvg(ctx, ConnectionPool.getInstance())); // uncomment this if want to try Svg
        // app.get("/", ctx ->  ctx.render("admin-frontpage.html")); // uncomment this if want to try materials
        // app.get("/", ctx ->  ctx.render("login-page.html")); // uncomment this is want to try login/createuser
            // Create a map to hold model attributes
            Map<String, Object> model = new HashMap<>();
            model.put("offer", offer);

            // Pass offer to the Thymeleaf template
            ctx.render("accept-or-deny-offer-customer", model);
        });
    }
}
