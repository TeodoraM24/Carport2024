package app.entities;

public class PartsList {
    private int partsListId;
    private Price price;

    public PartsList(int partsListId, Price price) {
        this.partsListId = partsListId;
        this.price = price;
    }

    public int getPartsListId() {
        return partsListId;
    }

    public void setPartsListId(int partsListId) {
        this.partsListId = partsListId;
    }
    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PartsList{" +
                "partsListId=" + partsListId +
                ", price=" + price +
                '}';
    }
}
