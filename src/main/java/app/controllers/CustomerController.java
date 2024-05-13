package app.controllers;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;
import app.persistence.CustomerMapper;

public class CustomerController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("loginpage", ctx -> ctx.render("customer-info-page.html"));
        app.get("logout", ctx -> logout(ctx));
        app.get("createuser", ctx -> ctx.render("create-user-page.html"));
        app.post("createuser", ctx -> createCustomer(ctx, connectionPool));
    }

    private static void createCustomer(Context ctx, ConnectionPool connectionPool) {

        String email = ctx.formParam("email");
        String email2=ctx.formParam("email2");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        int zip = Integer.parseInt(ctx.formParam("zip"));
        int phoneNumber = Integer.parseInt(ctx.formParam("phoneNumber"));
        String address=ctx.formParam("address");

        if (!email.equals(email2)) {
            ctx.attribute("message", "Dine e-mails matcher ikke! Prøv igen.");
            ctx.render("create-user-page.html");
            return;
        }

        if (password1.equals(password2)) {
            try {
                CustomerMapper.createUser(email, password1, firstName, lastName, zip, address, phoneNumber, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med e-mail: " + email + ". Nu skal du logge på.");
                ctx.render("create-user-page.html"); //her
            } catch (DatabaseException e) {
                ctx.attribute("message", "Den angivne e-mail findes allerede. Prøv igen, eller log ind");
                ctx.render("login-page.html"); //her
            }
        } else {
            ctx.attribute("message", "Dine to adgangskoder matcher ikke! Prøv igen");
            ctx.render("create-user-page.html");
        }
    }

    private static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }
}
