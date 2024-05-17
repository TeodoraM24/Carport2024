package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class AdminAwaitingCustomerController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/awaitingcustomers", ctx -> ctx.render("awaiting-customer.html"));
    }
}
