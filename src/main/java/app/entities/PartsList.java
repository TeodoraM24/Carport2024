package app.entities;

public class PartsList {
    private int partsListId;
    private double totalPriceWithTax;

    public PartsList(int partsListId, double totalPriceWithTax) {
        this.partsListId = partsListId;
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public PartsList(double totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public int getPartsListId() {
        return partsListId;
    }

    public void setPartsListId(int partsListId) {
        this.partsListId = partsListId;
    }

    public double getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public void setTotalPriceWithTax(double totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }
}
