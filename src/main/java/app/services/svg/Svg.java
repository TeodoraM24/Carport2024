package app.services.svg;

public class Svg {
    /***
     * Instances of SVG Strings to be used in html page
     */
    private static final String SVG_TEMPLATE = "<svg version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\"  width=\"%s\" \n" +
            "     preserveAspectRatio=\"xMinYMin\">";

    private static final String SVG_INNER_SVG = "<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
            "     x=\"%d\" y=\"%d\" width=\"%s\" height=\"%s\" viewBox=\"%s\" " +
            "     preserveAspectRatio=\"xMinYMin\">";

    private static final String SVG_ARROW_DEFS = "<defs>\n" +
            "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "    </defs>";

    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%.2f\" y=\"%.2f\" height=\"%f\" width=\"%f\" style=\"%s\" />";

    private static final String SVG_LINE_TEMPLATE = "<line x1=\"%f\" y1=\"%f\" x2=\"%f\" y2=\"%f\" style=\"%s\" />";

    private static final String SVG_TEXT_TEMPLATE = "<text style=\"text-anchor: middle\" transform=\"translate(%f,%f) rotate(%f)\">%s</text>";

    private StringBuilder svg = new StringBuilder();

    /***
     * The constructor for the inner and outer SVG with variable and fixed parameters.
     *
     * @param x the start of the framework on x-axis
     * @param y the start of the framework on y-axis
     * @param viewBox the parameter for viewbox
     * @param width the width
     * @param carportLength the chosen carport Length
     * @param carportWidth the chosen carport Width
     */
    public Svg(int x, int y, String viewBox, String width, int carportLength, int carportWidth) {

        //Appending outer SVG for measurement lines and arrows
        svg.append(String.format(SVG_TEMPLATE, x, y, viewBox, width));
        svg.append(SVG_ARROW_DEFS);

        //Vertical line, arrow and text for measurement
        int vLineX = 30;
        int vLineY = vLineX - 20;
        String vLineStyle = "stroke:#000000";
        //Horisontal line, arrow and text for measurement
        int hLineX = vLineX + 35;
        int hLineY = vLineY + 40;
        String hLineStyle = "stroke:#000000";

        //Vertical measurement
        addLine(vLineX, vLineY, vLineX, carportWidth + vLineY, vLineStyle);
        addArrow(vLineX, vLineY, vLineX, carportWidth + vLineY, vLineStyle);
        addText(vLineX - 10, carportWidth * 0.5 + vLineY, -90, carportWidth + " cm");

        //Horisontal measurement
        addLine(hLineX, carportWidth + hLineY, carportLength + hLineX, carportWidth + hLineY, hLineStyle);
        addArrow(hLineX, carportWidth + hLineY, carportLength + hLineX, carportWidth + hLineY, hLineStyle);
        addText(carportLength * 0.5 + hLineX, carportWidth + hLineX, 0, carportLength + " cm");

        // appending inner SVG for carport drawing
        svg.append(String.format(SVG_INNER_SVG, hLineX, vLineY, carportLength + 20, carportWidth, "0 0 " + carportLength + " " + carportWidth));
    }

    /***
     * Adds triangle and used for making rafter, beams and poles in CarportSvgTopDownView Class
     */
    public void addRectangle(double x, double y, double height, double width, String style) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, style));
    }

    /***
     * Adds line and used for making measurement line, cross in CarportSvgTopDownView/Svg Class
     */
    public void addLine(double x1, double y1, double x2, double y2, String style) {
        svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2, style));
    }

    /***
     * Adds line and used for making measurement arrow in Svg Class
     */
    public void addArrow(double x1, double y1, double x2, double y2, String style) {
        String arrowStyle = String.format("marker-start: url(#beginArrow); marker-end: url(#endArrow);", style);
        addLine(x1, y1, x2, y2, arrowStyle);
    }

    /***
     * Adds text and used for making measurement text in Svg Class
     */
    public void addText(double x, double y, double rotation, String text) {
        svg.append(String.format(SVG_TEXT_TEMPLATE, x, y, rotation, text));
    }

    @Override
    public String toString() {
        return svg.append("</svg>").toString();
    }
}
