package app.controllers;

import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.AdminRequestMapper;
import app.persistence.ConnectionPool;
import app.services.CarportSvgSideView;
import app.services.CarportSvgTopDownView;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;

public class SvgController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/Svg", ctx -> displaySvg(ctx, connectionPool));

        app.post("/showDrawing", ctx -> {
            displayDrawing(ctx, connectionPool);
        });
    }

    private static void displayDrawing(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int customerRequestId = AdminRequestController.getSessionCurrentRequestId(ctx);
        CustomerRequest customerRequest = AdminRequestMapper.getCustomerRequest(customerRequestId, connectionPool);
        int carportLength = customerRequest.getRequestLength();
        int carportWidth = customerRequest.getRequestWidth();
        int carportHeight = customerRequest.getRequestHeight();

        Locale.setDefault(new Locale("US"));
        CarportSvgTopDownView svgTopDownView = new CarportSvgTopDownView(carportLength, carportWidth);
        CarportSvgSideView svgSideView = new CarportSvgSideView(carportLength, carportHeight);
        ctx.attribute("svgTopDownView", svgTopDownView.toString());
        ctx.attribute("svgSideView", svgSideView.toString());
        ctx.render("Svg-page.html");
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