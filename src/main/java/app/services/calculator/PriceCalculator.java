package app.services.calculator;

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
        this.salesPrice = purchasePrice * 3.0;
    }

    public PriceCalculator(List<PartsListItem> partsListItems, double salesPrice) {
        this.partsListItems = partsListItems;
        this.purchasePrice = calcPurchasePrice();
        this.salesPrice = salesPrice;
    }

    public double calcPurchasePrice() {
        double purchasePrice = 0.0;
        for (PartsListItem p : partsListItems) {
            double price = p.getMaterial().getPrice();
            BigDecimal amount = new BigDecimal(p.getAmount()).setScale(2, RoundingMode.HALF_UP);
            purchasePrice += (price * amount.doubleValue());
        }

        return purchasePrice;
    }

    public double getSalesPrice() {
        BigDecimal salesPriceWithTax = new BigDecimal(salesPrice).setScale(2, RoundingMode.HALF_UP);

        return salesPriceWithTax.doubleValue();
    }

    public double calcPriceWithoutTax() {
        BigDecimal salesPriceWithoutTax = new BigDecimal(salesPrice / 1.20).setScale(2, RoundingMode.HALF_UP);

        return salesPriceWithoutTax.doubleValue();
    }

    public double calcCoverage() {
        double profit = calcPriceWithoutTax() - purchasePrice;
        BigDecimal twoDecimalRoundUp = new BigDecimal(profit / calcPriceWithoutTax() * 100.0).setScale(2, RoundingMode.HALF_UP);

        return twoDecimalRoundUp.doubleValue();
    }
}
