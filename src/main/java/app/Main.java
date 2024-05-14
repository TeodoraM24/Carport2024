package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.AdminOfferController;
import app.controllers.AdminRequestController;
import app.controllers.CustomerController;
import app.controllers.MaterialController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {

    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        AdminRequestController.addRoutes(app, ConnectionPool.getInstance());
        AdminOfferController.addRoutes(app, ConnectionPool.getInstance());
        MaterialController.addRoutes(app, ConnectionPool.getInstance());
        CustomerController.addRoutes(app, ConnectionPool.getInstance());
    }
}