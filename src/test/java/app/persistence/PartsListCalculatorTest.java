package app.persistence;

import app.services.PartsListCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class PartsListCalculatorTest {
    private PartsListCalculator partsListCalculator;
    @BeforeEach
    void setup() {
        partsListCalculator = new PartsListCalculator(900, 600, 210);
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

}
