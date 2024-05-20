package app.services.svg;

public class CarportSvgTopDownView {
    private final int length;
    private final int width;
    private final Svg carportSvg;

    /***
     * The constructor for the inner SVG carport top down view with length and width parameters
     * and methods adding beams, rafters, poles and cross.
     *
     * @param length the chosen length for the carport
     * @param width the chosen length for the carport
     */
    public CarportSvgTopDownView(int length, int width) {
        this.length = length;
        this.width = width;
        carportSvg = new Svg(0, 0, "0 0 " + (length + 75) + " " + (width + 90), "50%", length, width);
        carportSvg.addRectangle(0, 0, width, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPoles();
        addCross();
    }

    /***
     * method for adding beams to the SVG carport with addRectangle method from SVG class
     */
    private void addBeams() {
        carportSvg.addRectangle(0, 35, 4.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0, width - 35 - 4.5, 4.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    /***
     * method for adding rafters to the SVG carport with addRectangle method from SVG class
     */
    private void addRafters() {
        for (double i = 0; i < length; i += 55) {
            carportSvg.addRectangle(i, 0.0, width, 4.5, "stroke:#000000; fill: #ffffff");
        }
        carportSvg.addRectangle(length - 4.5, 0.0, width, 4.5, "stroke:#000000; fill: #ffffff");
    }

    /***
     * method for adding poles to the SVG carport with addRectangle method from SVG class
     */
    private void addPoles() {
        carportSvg.addRectangle(107, 32, 9.7, 10, "stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(107, width - 32 - 10, 9.7, 10, "stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(length - 30, 32, 9.7, 10, "stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(length - 30, width - 32 - 10, 9.7, 10, "stroke:#000000; fill: #ffffff");

        if (length >= 470) {
            carportSvg.addRectangle((length + 107 - 30) / 2, 32, 9.7, 10, "stroke:#000000; fill: #ffffff");
            carportSvg.addRectangle((length + 107 - 30) / 2, width - 32 - 10, 9.7, 10, "stroke:#000000; fill: #ffffff");
        }
    }

    /***
     * method for adding cross to the SVG carport with addLine method from SVG class
     */
    private void addCross() {
        carportSvg.addLine(55, 35, length - 25, width - 35, "stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine(55, width - 35, length - 25, 35, "stroke:#000000; stroke-dasharray: 5 5;");
    }

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
