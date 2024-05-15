package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.AdminCustomerRequestController;
import app.controllers.CustomerController;
import app.controllers.CustomerRequestController;
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
        //CustomerController.addRoutes(app, ConnectionPool.getInstance());

        app.get("/", ctx ->  ctx.render("admin-frontpage.html"));
        CustomerController.addRoutes(app, ConnectionPool.getInstance());
        AdminCustomerRequestController.addRoutes(app, ConnectionPool.getInstance());
        CustomerRequestController.addRoutes(app, ConnectionPool.getInstance());

    }
}