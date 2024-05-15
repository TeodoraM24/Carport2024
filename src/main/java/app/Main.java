package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.AdminOfferController;
import app.controllers.AdminRequestController;
import app.controllers.CustomerController;
import app.controllers.SvgController;
import app.controllers.MaterialController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import static app.controllers.SvgController.displaySvg;


public class Main {

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

        //app.get("/", ctx -> displaySvg(ctx, ConnectionPool.getInstance())); // uncomment this if want to try Svg
        // app.get("/", ctx ->  ctx.render("admin-frontpage.html")); // uncomment this if want to try materials
        // app.get("/", ctx ->  ctx.render("login-page.html")); // uncomment this is want to try login/createuser
    }
}