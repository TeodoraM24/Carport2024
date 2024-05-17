package app.controllers;

import app.entities.Admin;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import app.persistence.LoginMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class LoginController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/login-page", ctx -> ctx.render("login-page.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));
    }

    private static void login(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        boolean isAdmin= LoginMapper.isAdmin(email, connectionPool);

        if (isAdmin){
            Admin admin = AdminMapper.logInd(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", admin);
            ctx.attribute("message", "Du er nu logget ind");
            ctx.redirect("loginpage-admin");
        } else {
            Customer customer = CustomerMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", customer);
            ctx.attribute("message", "Du er nu logget ind");
            ctx.redirect("loginpage-customer");
        }
    }
}
