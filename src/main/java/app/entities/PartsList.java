package app.entities;

public class PartsList {
    private int partsListId;
    private int priceId;

    public PartsList(int partsListId, int priceId) {
        this.partsListId = partsListId;
        this.priceId = priceId;
    }

    public int getPartsListId() {
        return partsListId;
    }

    public void setPartsListId(int partsListId) {
        this.partsListId = partsListId;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    @Override
    public String toString() {
        return "PartsList{" +
                "partsListId=" + partsListId +
                ", priceId=" + priceId +
                '}';
    }
}
