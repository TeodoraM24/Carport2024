package app.services.svg;

public class CarportSvgSideView {
    private final int length;
    private final int height;
    private final Svg carportSvg;

    /***
     * The constructor for the inner SVG carport side view with length and height parameters
     * and methods adding beams, rafters and poles.
     *
     * @param length the chosen length for the carport
     * @param height the chosen height for the carport
     */
    public CarportSvgSideView(int length, int height) {
        this.length = length;
        this.height = height;
        carportSvg = new Svg(0, 0, "0 0 " + (length + 75) + " " + (height + 90), "50%", length, height);
        carportSvg.addRectangle(0, 0, height, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addPoles();
        addRafters();
        addBeams();
    }

    /***
     * method for adding beams to the SVG carport with addRectangle method from SVG class
     */
    private void addBeams() {
        carportSvg.addRectangle(0, 0, 17.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0, 20, 17.5, length, "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    /***
     * method for adding rafters to the SVG carport with addRectangle method from SVG class
     */
    private void addRafters() {
        for (double i = 0; i < length; i += 55) {
            carportSvg.addRectangle(i, 17.5, 18.5, 4.5, "stroke:#000000; fill: #ffffff");
        }
        carportSvg.addRectangle(length - 4.5, 17.5, 18.5, 4.5, "stroke:#000000; fill: #ffffff");
    }

    /***
     * method for adding poles to the SVG carport with addRectangle method from SVG class
     */
    private void addPoles() {
        carportSvg.addRectangle(107, 32, height - 32, 10, "stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(length - 30, 32, height - 32, 10, "stroke:#000000; fill: #ffffff");

        if (length >= 470) {
            carportSvg.addRectangle((length + 107 - 30) / 2, 32, height - 32, 10, "stroke:#000000; fill: #ffffff");
        }
    }

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
