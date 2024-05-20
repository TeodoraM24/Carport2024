package app.controllers;

import app.controllers.admin.AdminRequestController;
import app.entities.Carport;
import app.entities.CustomerRequest;
import app.exceptions.DatabaseException;
import app.persistence.admin.AdminRequestMapper;
import app.persistence.ConnectionPool;
import app.services.svg.CarportSvgSideView;
import app.services.svg.CarportSvgTopDownView;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Locale;

public class SvgController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("/showDrawing", ctx -> displayDrawing(ctx, connectionPool));
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
        ctx.render("svg/Svg-page.html");
    }

    /**
     * Renders the SVG page with top down and side view of carport with chosen width, length and height
     *
     * @param ctx            Context parameter used to obtain information related to the page
     */
    public static void displaySvg(Carport carport, Context ctx) {
        Locale.setDefault(new Locale("US"));
        CarportSvgTopDownView svgTopDownView = new CarportSvgTopDownView(carport.getLength(), carport.getWidth());
        CarportSvgSideView svgSideView = new CarportSvgSideView(carport.getLength(), carport.getHeight());
        ctx.attribute("svgTopDownView", svgTopDownView.toString());
        ctx.attribute("svgSideView", svgSideView.toString());
        ctx.render("svg/Svg-page.html");
    }
}