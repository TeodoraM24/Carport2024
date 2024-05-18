package app.entities;

public class Carport {
    private int carportId;
    private int width;
    private int height;
    private int length;
    private Price price;

    public Carport(int carportId, int width, int height, int length, Price price) {
        this.carportId = carportId;
        this.width = width;
        this.height = height;
        this.length = length;
        this.price = price;
    }

    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Carport{" +
                "carportId=" + carportId +
                ", width=" + width +
                ", height=" + height +
                ", length=" + length +
                ", price=" + price +
                '}';
    }
}
