package app.persistence;


import app.entities.Material;
import app.entities.PartsListItem;
import app.services.PriceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceCalculatorTest {
    private PriceCalculator priceCalculator;
    @BeforeEach
    void setup() {
        List<PartsListItem> partsListItems = new ArrayList<>();
        partsListItems.add(new PartsListItem(new Material("",0,0,0,7500),1,"",""));
        priceCalculator = new PriceCalculator(partsListItems);
    }

    @Test
    void testGetSalesPrice() {
        double expectedSalesPrice = 22500;

        double actualSalesPrice = priceCalculator.getSalesPrice();

        assertEquals(expectedSalesPrice, actualSalesPrice);
    }

    @Test
    void testGetPriceWithoutTax() {
        double expectedPriceWithoutTax = 18000;

        double actualPriceWithoutTax = priceCalculator.calcPriceWithoutTax();

        assertEquals(expectedPriceWithoutTax, actualPriceWithoutTax);
    }

    @Test
    void testCalcCoverage() {
        double expectedCoverage = 58.33;

        double actualCoverage = priceCalculator.calcCoverage();

        assertEquals(expectedCoverage, actualCoverage);
    }
}
