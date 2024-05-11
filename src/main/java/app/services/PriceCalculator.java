package app.services;

import app.entities.PartsListItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PriceCalculator {
    private List<PartsListItem> partsListItems;
    private double purchasePrice;
    private double salesPrice;

    public PriceCalculator(List<PartsListItem> partsListItems) {
        this.partsListItems = partsListItems;
        this.purchasePrice = calcPurchasePrice();
        this.salesPrice = getSalesPrice();
    }

    public double calcPurchasePrice() {
        int purchasePrice = 0;
        for (PartsListItem p : partsListItems) {
            int price = p.getMaterial().getPrice();
            int amount = p.getAmount();
            purchasePrice += price * amount;
        }
        return purchasePrice;
    }

    public double getSalesPrice() {
        return purchasePrice * 3; // we dont know their markup..
    }

    public double calcPriceWithoutTax() {
        return salesPrice / 1.25;
    }

    public double calcCoverage() {
        double profit = calcPriceWithoutTax() - purchasePrice;
        BigDecimal twoDecimalRoundUp = new BigDecimal(profit / calcPriceWithoutTax() * 100).setScale(2, RoundingMode.HALF_UP);
        return twoDecimalRoundUp.doubleValue();
    }
}
