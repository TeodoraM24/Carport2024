package app.controllers;

import app.entities.Admin;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/loginpage-admin", ctx -> {
            ctx.render("admin-frontpage.html");
        });
        app.get("/admin/logout", ctx -> logout(ctx));
    }

    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }
}
