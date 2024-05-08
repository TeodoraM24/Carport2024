package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.MaterialController;
import app.controllers.CustomerController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "HigAbt60ig";
    private static final String URL = "jdbc:postgresql://161.35.195.156/%s?currentSchema=public";
    private static final String DB = "carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        MaterialController.addRoutes(app, ConnectionPool.getInstance());

        app.get("/", ctx ->  ctx.render("admin-frontpage.html"));
        app.get("/", ctx ->  ctx.render("login-page.html")); //change
        CustomerController.addRoutes(app, connectionPool);

    }
}