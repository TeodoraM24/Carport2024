package app.controllers;

import app.entities.Customer;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.validators.EmailValidator;
import app.validators.PasswordValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import app.persistence.CustomerMapper;

public class CustomerController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("loginpage", ctx -> logInd(ctx, connectionPool));
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
            ctx.render("customer-info-page.html");
        }
        catch (DatabaseException e)
        {
            ctx.attribute("message", e.getMessage() );
            ctx.render("login-page.html"); //same here
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
        String address=ctx.formParam("address");

        if(EmailValidator.isValidEmail(email)) {
            ctx.attribute("message", "Den indtastede mail er ikke gyldig. Prøv igen");
            ctx.render("create-user-page.html");
            return;
        }

        if(PasswordValidator.isValidPassword(password1)){
            ctx.attribute("message", "Det indtastede kodeord er ikke gyldigt. Den skal minimum indeholde 8 tegn, et stort bogstav, et småt bogstav og et nummer. Prøv igen.");
            return;
        }

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
