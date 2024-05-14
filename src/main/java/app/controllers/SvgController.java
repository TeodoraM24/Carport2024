package app.controllers;

import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.CarportSvgSideView;
import app.services.CarportSvgTopDownView;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;

public class SvgController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/Svg", ctx -> displaySvg(ctx, connectionPool));
    }

    /**
     * Renders the SVG page with top down and side view of carport with chosen width, length and height
     *
     * @param ctx            Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve data from database
     */
    public static void displaySvg(Context ctx, ConnectionPool connectionPool) {
        Locale.setDefault(new Locale("US"));
        CarportSvgTopDownView svgTopDownView = new CarportSvgTopDownView(780, 600);
        CarportSvgSideView svgSideView = new CarportSvgSideView(780, 230);
        ctx.attribute("svgTopDownView", svgTopDownView.toString());
        ctx.attribute("svgSideView", svgSideView.toString());
        ctx.render("Svg-page.html");
    }
}