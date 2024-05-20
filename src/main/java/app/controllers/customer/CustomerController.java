package app.controllers.customer;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.validators.CityValidator;
import app.validators.EmailValidator;
import app.validators.PasswordValidator;
import io.javalin.Javalin;
import io.javalin.http.Context;
import app.persistence.customer.CustomerMapper;

public class CustomerController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("createuser", ctx -> ctx.render("login/create-user-page.html"));
        app.post("createuser", ctx -> {
            createCustomer(ctx, connectionPool);
        });

        app.get("/customerinfo", ctx -> ctx.render("customer/customer-info-frontpage.html"));
    }

    private static void createCustomer(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        String email = ctx.formParam("email");
        String email2 = ctx.formParam("email2");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");
        int zip = Integer.parseInt(ctx.formParam("zip"));
        String cityName = ctx.formParam("cityName");
        int phoneNumber = Integer.parseInt(ctx.formParam("phoneNumber"));
        String address = ctx.formParam("address");

        if (EmailValidator.isValidEmail(email)) {
            if (PasswordValidator.isValidPassword(password1)) {
                if (password1.equals(password2)) {
                    if (email.equals(email2)) {
                        String capitalizedCity = CityValidator.capitalizeCustom(cityName);
                        if (CustomerMapper.cityChecker(zip, capitalizedCity, connectionPool)) {
                            try {
                                CustomerMapper.createUser(email, password1, firstName, lastName, zip, address, phoneNumber, connectionPool);
                                ctx.attribute("message", "Du er hermed oprettet med e-mail: " + email + ". Nu skal du logge på.");
                                ctx.render("login/create-user-page.html");
                            } catch (DatabaseException e) {
                                ctx.attribute("message", "Den angivne e-mail findes allerede. Prøv igen, eller log ind");
                                ctx.render("login/login-page.html");
                            }
                        } else {
                            ctx.attribute("message", "Postnummer og by matcher ikke. Prøv igen.");
                        }
                    } else {
                        ctx.attribute("message", "De indtastede mails mather ikke. Prøv igen.");
                    }
                } else {
                    ctx.attribute("message", "De indtastede kodeord matcher ikke. Prøv igen.");
                }
            } else {
                ctx.attribute("message", "Den indtastede kodeord er ikke gyldig. Det skal minimum indeholde 8 tegn, et stort og et småt bogstav samt et tal. Prøv igen.");
            }
        } else {
            ctx.attribute("message", "Den indtastede mail er ikke gyldig. Prøv igen.");
        }
        ctx.render("login/create-user-page.html");
    }
}
