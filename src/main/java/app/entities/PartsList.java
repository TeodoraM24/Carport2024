package app.entities;

public class PartsList {
    private int partsListId;
    private double purchasePrice;
    private double totalPriceWithTax;
    private double coverageRatio;

    public PartsList(int partsListId, double purchasePrice, double totalPriceWithTax, double coverageRatio) {
        this.partsListId = partsListId;
        this.purchasePrice = purchasePrice;
        this.totalPriceWithTax = totalPriceWithTax;
        this.coverageRatio = coverageRatio;
    }

    public int getPartsListId() {
        return partsListId;
    }

    public void setPartsListId(int partsListId) {
        this.partsListId = partsListId;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public void setTotalPriceWithTax(double totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public double getCoverageRatio() {
        return coverageRatio;
    }

    public void setCoverageRatio(double coverageRatio) {
        this.coverageRatio = coverageRatio;
    }
}
