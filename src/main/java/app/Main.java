package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.*;
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
        AdminController.addRoutes(app, ConnectionPool.getInstance());
        AdminCustomerRequestController.addRoutes(app, ConnectionPool.getInstance());
        AdminRequestController.addRoutes(app, ConnectionPool.getInstance());
        AdminAwaitingCustomerController.addRoutes(app, ConnectionPool.getInstance());
        CustomerController.addRoutes(app, ConnectionPool.getInstance());
        CustomerRequestController.addRoutes(app, ConnectionPool.getInstance());
        InvoiceController.addRoutes(app, ConnectionPool.getInstance());
        LoginController.addRoutes(app, ConnectionPool.getInstance());
        MaterialController.addRoutes(app, ConnectionPool.getInstance());
        OfferController.addRoutes(app, ConnectionPool.getInstance());
        SvgController.addRoutes(app, ConnectionPool.getInstance());


        //app.get("/", ctx ->  ctx.render("admin-frontpage.html"));
        //app.get("/", ctx -> displaySvg(ctx, ConnectionPool.getInstance())); // uncomment this if want to try Svg
        // app.get("/", ctx ->  ctx.render("admin-frontpage.html")); // uncomment this if want to try materials
        // app.get("/", ctx ->  ctx.render("login-page.html")); // uncomment this is want to try login/createuser

    }
}