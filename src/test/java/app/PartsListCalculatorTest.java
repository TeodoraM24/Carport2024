package app;

import app.entities.Material;
import app.entities.PartsListItem;
import app.services.calculator.PartsListCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class PartsListCalculatorTest {
    private PartsListCalculator partsListCalculator;
    @BeforeEach
    void setup() {
        Material post = new Material("97x97 mm. trykimp. Stolpe", 97, 97);
        Material beam = new Material("45x195 mm. spærtræ ubh.", 195, 45);
        Material rafter = new Material("45x195 mm. spærtræ ubh.", 97, 97);
        partsListCalculator = new PartsListCalculator(900, 600, 210, post, beam, rafter);
    }

    @Test
    void testCalculateCarport() {
        int postAmount = 8;
        int beamAmount = 8;
        int rafterAmount = 16;
        int expectedTotalAmount = postAmount + beamAmount + rafterAmount;
        int actualTotalAmount = 0;

        partsListCalculator.calcCarport();
        List<PartsListItem> partsListItems = partsListCalculator.getPartsListItems();
        for (PartsListItem p: partsListItems) {
            actualTotalAmount += p.getAmount();
        }

        assertEquals(expectedTotalAmount, actualTotalAmount);
    }

    @Test
    void testCalculatePostQuantity(){
        int expectedQuantity = 8;

        int actualQuantity = partsListCalculator.calcPostQuantity();

        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    void testCalculateAvgDistanceBetweenPosts() {
        int expectedAvgDistance = 256;

        int actualAvgDistance = partsListCalculator.calcAvgDistanceBetweenPosts();

        assertEquals(expectedAvgDistance, actualAvgDistance);
    }

    @Test
    void testCalculateRemainingDistancePost() {
        int expectedRemainingDistance = 258;

        int actualRemainingDistance = partsListCalculator.calcRemainingDistance();

        assertEquals(expectedRemainingDistance, actualRemainingDistance);
    }

    @Test
    void testCalcAmountOfRafters() {
        int expectedAmount = 16;
        int rafterWidthMM = 45;

        int actualAmount = partsListCalculator.calcAmountOfRafters(rafterWidthMM);

        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    void testCalcRafterLength() {
        int expectedLength = 600;

        int actualLength = partsListCalculator.calcRafterLength();

        assertEquals(expectedLength, actualLength);
    }

}
