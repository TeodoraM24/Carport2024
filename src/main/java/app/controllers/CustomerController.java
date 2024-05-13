package app.controllers;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;
import app.persistence.CustomerMapper;

public class CustomerController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        //app.post("login", ctx -> logInd(ctx, connectionPool));
        //app.get("login", ctx -> ctx.render("login-page"));
        app.get("logout", ctx -> logout(ctx));
        app.get("createuser", ctx -> ctx.render("create-user-page.html"));
        app.post("createuser", ctx -> createCustomer(ctx, connectionPool));
    }

    public static void logInd(Context ctx, ConnectionPool connectionPool){

        String email = ctx.formParam("email");
        String password = ctx.formParam("password");


        try
        {
            Customer customer = CustomerMapper.logInd(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", customer);

            ctx.attribute("message", "Du er nu logget ind");
            ctx.render("index.html"); //skal ændres
        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage() );
            ctx.render("index.html"); //same here
        }

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

        if (!email.equals(email2)) {
            ctx.attribute("message", "Dine e-mails matcher ikke! Prøv igen.");
            ctx.render("create-user-page.html");
            return;
        }

        if (password1.equals(password2)) {
            try {
                CustomerMapper.createUser(email, password1, firstName, lastName, zip, phoneNumber, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med e-mail: " + email + ". Nu skal du logge på.");
                ctx.render("index.html"); //her
            } catch (DatabaseException e) {
                ctx.attribute("message", "Den angivne e-mail findes allerede. Prøv igen, eller log ind");
                ctx.render("index.html"); //her
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
