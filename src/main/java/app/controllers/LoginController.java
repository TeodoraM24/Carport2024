package app.controllers;

import app.entities.Admin;
import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.admin.AdminMapper;
import app.persistence.ConnectionPool;
import app.persistence.customer.CustomerMapper;
import app.persistence.LoginMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class LoginController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/", ctx -> ctx.render("customer/carport-index.html"));
        app.get("/login-page", ctx -> ctx.render("login/login-page.html"));
        app.get("/loginpage-customer", ctx -> ctx.render("customer/carport-index.html"));
        app.get("/loginpage-admin", ctx -> ctx.render("admin/admin-frontpage.html"));
        app.get("/admin/logout", ctx -> logout(ctx));
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.get("/logout", ctx -> logout(ctx));
    }

    public static void login(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            boolean isAdmin = LoginMapper.isAdmin(email, connectionPool);
            if (isAdmin) {
                Admin admin = AdminMapper.login(email, password, connectionPool);
                ctx.sessionAttribute("currentUser", admin);
                ctx.attribute("message", "Du er nu logget ind");
                ctx.redirect("/loginpage-admin");
            } else {
                Customer customer = CustomerMapper.login(email, password, connectionPool);
                ctx.sessionAttribute("currentUser", customer);
                ctx.attribute("message", "Du er nu login");
                ctx.redirect("/loginpage-customer");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login/login-page.html");
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }
}
