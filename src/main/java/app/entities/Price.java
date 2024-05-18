package app.entities;

public class Price {
    private int priceId;
    private double purchasePrice;
    private double salesPrice;
    private double priceWithoutTax;
    private double coverage;

    public Price(int priceId, double purchasePrice, double salesPrice, double coverage) {
        this.priceId = priceId;
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
        this.coverage = coverage;
    }

    public Price(double purchasePrice, double salesPrice, double priceWithoutTax, double coverage) {
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
        this.priceWithoutTax = priceWithoutTax;
        this.coverage = coverage;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public double getPriceWithoutTax() {
        return priceWithoutTax;
    }

    public void setPriceWithoutTax(double priceWithoutTax) {
        this.priceWithoutTax = priceWithoutTax;
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        return "Price{" +
                "priceId=" + priceId +
                ", purchasePrice=" + purchasePrice +
                ", salesPrice=" + salesPrice +
                ", priceWithoutTax=" + priceWithoutTax +
                ", coverage=" + coverage +
                '}';
    }
}
