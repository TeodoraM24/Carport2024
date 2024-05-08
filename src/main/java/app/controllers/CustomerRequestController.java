package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CustomerMapper;
import io.javalin.http.Context;

public class CustomerRequestController {

    private void addDimensions() {

    }

    private void sendRequest(Context ctx, ConnectionPool connectionPool) {

        int length = Integer.parseInt(ctx.formParam("length"));
        int width = Integer.parseInt(ctx.formParam("width"));
        int height = Integer.parseInt(ctx.formParam("height"));

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

}
