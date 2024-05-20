package app.controllers.admin;

import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.admin.MaterialMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class MaterialController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("material", ctx -> displayMaterialsPage(ctx, connectionPool));
        app.post("material", ctx -> displayMaterialsPage(ctx, connectionPool));
        app.post("addmaterial", ctx -> addMaterial(ctx, connectionPool));
        app.post("deletematerial", ctx -> deleteMaterial(ctx, connectionPool));
        app.post("editmaterial", ctx -> editMaterial(ctx, connectionPool));
        app.post("updatematerial", ctx -> updateMaterial(ctx, connectionPool));
    }

    /**
     * Updates materials by selecting a material from the material table in database and input new data in the material
     *
     * @param ctx Context parameter used to getting material information, creating a list of Material to use in html page and making message retrievable
     * @param connectionPool ConnectionPool used to retrieve materials information and to update in database
     * @throws DatabaseException Throws exception if database connection/sql fails
     */
    private static void updateMaterial(Context ctx, ConnectionPool connectionPool) {
        try {
            int materialId = Integer.parseInt(ctx.formParam("materialId"));
            String description = ctx.formParam("description");
            int height = Integer.parseInt(ctx.formParam("height"));
            int width = Integer.parseInt(ctx.formParam("width"));
            int length = Integer.parseInt(ctx.formParam("length"));
            double price = Double.parseDouble(ctx.formParam("price"));

            MaterialMapper.updateMaterial(materialId, description, height, width, length, price, connectionPool);

            List<Material> materialList = MaterialMapper.getAllMaterials(connectionPool);
            ctx.attribute("materialList", materialList);
            ctx.render("admin/materials-page.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("admin/materials-page.html");
        }
    }

    /**
     * Edits materials by selecting from the material table in database
     *
     * @param ctx Context parameter used to getting material information, creating an object type of Material to use in html page and making message retrievable
     * @param connectionPool ConnectionPool used to retrieve materials information and to edit in database
     * @throws DatabaseException Throws exception if database connection/sql fails
     */
    private static void editMaterial(Context ctx, ConnectionPool connectionPool) {

        try {
            int materialId = Integer.parseInt(ctx.formParam("materialId"));
            Material material = MaterialMapper.getMaterialById(materialId, connectionPool);
            ctx.attribute("material", material);
            ctx.render("admin/edit-materials-page.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("admin/materials-page.html");
        }
    }

    /**
     * Renders the materials page after redirecting from admin page
     * Gets all necessary data from database to be displayed on page
     *
     * @param ctx Context parameter used to obtain information related to the page
     * @param connectionPool ConnectionPool used to retrieve materials data from database
     * @throws DatabaseException Throws exception if database connection/sql fails
     */
    private static void displayMaterialsPage(Context ctx, ConnectionPool connectionPool) {

        List<Material> materialList = null;
        try {
            materialList = MaterialMapper.getAllMaterials(connectionPool);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        ctx.attribute("materialList", materialList);
        ctx.render("admin/materials-page.html");
    }

    /**
     * Adds materials by user input to the material table in database
     *
     * @param ctx Context parameter used to getting material information, creating a list of materials to use in html page and making message retrievable
     * @param connectionPool ConnectionPool used to retrieve materials information and to save in database
     * @throws DatabaseException Throws exception if database connection/sql fails
     */
    private static void addMaterial(Context ctx, ConnectionPool connectionPool) {

        String description = ctx.formParam("description");
        int height = Integer.parseInt(ctx.formParam("height"));
        int width = Integer.parseInt(ctx.formParam("width"));
        int length = Integer.parseInt(ctx.formParam("length"));
        double price = Double.parseDouble(ctx.formParam("price"));

        try {
            MaterialMapper.addMaterial(description, height, width, length, price, connectionPool);
            List<Material> materialList = MaterialMapper.getAllMaterials(connectionPool);
            ctx.attribute("materialList", materialList);
            ctx.render("admin/materials-page.html");
        } catch (DatabaseException e) {
            ctx.attribute("message", "Something went wrong. Try again.");
            ctx.render("admin/materials-page.html");
        }
    }

    /**
     * Deletes materials by selecting from the material table in database
     *
     * @param ctx Context parameter used to getting material information, creating a list of materials to use in html page and making message retrievable
     * @param connectionPool ConnectionPool used to retrieve materials information and to delete in database
     * @throws DatabaseException Throws exception if database connection/sql fails
     */
    private static void deleteMaterial(Context ctx, ConnectionPool connectionPool) {

        int materialId = Integer.parseInt(ctx.formParam("materialId"));

        try {
            MaterialMapper.deleteMaterial(materialId, connectionPool);
            List<Material> materialList = MaterialMapper.getAllMaterials(connectionPool);
            ctx.attribute("materialList", materialList);
            ctx.render("admin/materials-page.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("admin/materials-page.html");
        }
    }
}
