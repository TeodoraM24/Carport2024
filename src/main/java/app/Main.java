package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        app.get("/", ctx -> ctx.render("carport-index.html"));
        app.get("/carport-form", ctx -> ctx.render("carport-form.html"));
        app.get("/customer-info-page", ctx -> ctx.render("customer-info-page.html"));

        app.post("/carport-offer-sent", ctx -> ctx.render("carport-offer-sent.html"));
    }
}