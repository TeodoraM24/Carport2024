package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.InvoiceController;
import app.controllers.MaterialController;
import app.controllers.CustomerController;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.InvoiceMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
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

        InvoiceController.addRoutes(app, ConnectionPool.getInstance());
    }
}