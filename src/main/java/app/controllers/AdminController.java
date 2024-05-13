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
        app.get("loginpage-admin", ctx -> {
            logInd(ctx, connectionPool);
            //render admin page
        });
        app.get("/admin/logout", ctx -> logout(ctx));
    }

    public static void logInd(Context ctx, ConnectionPool connectionPool){
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            Admin admin = AdminMapper.logInd(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", admin);
            ctx.attribute("message", "Du er nu logget ind");

        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage() );
            ctx.render("login-page.html");
        }
    }

    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }
}
