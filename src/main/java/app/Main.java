package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
import app.controllers.admin.*;
import app.controllers.customer.CustomerController;
import app.controllers.customer.CustomerRequestController;
import app.controllers.customer.InvoiceController;
import app.controllers.customer.OfferController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        LoginController.addRoutes(app, ConnectionPool.getInstance());
        AdminOfferController.addRoutes(app,ConnectionPool.getInstance());
        AdminRequestController.addRoutes(app, ConnectionPool.getInstance());
        AwaitingCustomerController.addRoutes(app, ConnectionPool.getInstance());
        MaterialController.addRoutes(app, ConnectionPool.getInstance());
        OfferController.addRoutes(app, ConnectionPool.getInstance());
        CustomerController.addRoutes(app, ConnectionPool.getInstance());
        CustomerRequestController.addRoutes(app, ConnectionPool.getInstance());
        InvoiceController.addRoutes(app, ConnectionPool.getInstance());
        SvgController.addRoutes(app, ConnectionPool.getInstance());
    }
}