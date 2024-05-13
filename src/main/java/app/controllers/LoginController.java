package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.LoginMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class LoginController {
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/login", ctx -> checkUserRole(ctx, connectionPool));
    }

    private static void checkUserRole(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        boolean isAdmin= LoginMapper.isAdmin(email, connectionPool);

        if (isAdmin){
            ctx.redirect("loginpage-admin");
        } else {
            ctx.redirect("loginpage");
        }
    }
}
