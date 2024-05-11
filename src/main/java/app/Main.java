package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.CustomerController;
import app.controllers.CustomerRequestController;
import app.entities.CustomerRequest;
import app.persistence.ConnectionPool;
import app.persistence.CustomerRequestMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "HigAbt60ig";
    private static final String URL = "jdbc:postgresql://161.35.195.156/%s?currentSchema=public";
    private static final String DB = "carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

         // Routing
        //MaterialController.addRoutes(app, ConnectionPool.getInstance());
        CustomerController.addRoutes(app, ConnectionPool.getInstance());
        //CustomerRequestController.addRoutes(app, ConnectionPool.getInstance());

        // app.get("/", ctx ->  ctx.render("admin-frontpage.html")); // uncomment this if want to try materials
        //app.get("/", ctx ->  ctx.render("login-page.html")); // uncomment this is want to try login/createuser


       /*app.get("/", ctx -> {
            // Retrieve customer requests from the database
            List<CustomerRequest> customerRequests = CustomerRequestMapper.getAllCustomerRequests(1, connectionPool);

            // Create a map to hold model attributes
            Map<String, Object> model = new HashMap<>();
            model.put("customerRequests", customerRequests);

            // Pass customer requests to the Thymeleaf template
            ctx.render("customer-request-status", model);
        }); */ // hvis man vil tjekke status ud, skal slettes senere
    }
}
