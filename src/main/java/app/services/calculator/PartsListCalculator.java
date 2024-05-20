package app.services.calculator;

import app.entities.Material;
import app.entities.PartsListItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PartsListCalculator {
    private List<PartsListItem> partsListItems = new ArrayList<>();
    private int carportWidth;
    private int carportLength;
    private int carportHeight;
    private Integer[] lengthsOfBeamRafterPost = {300, 360, 420, 480, 540, 600};
    private int removeLengthBehind = 30;
    private int removeLengthFront = 100;
    private int removeTotalLength = removeLengthFront + removeLengthBehind;
    private Material post;
    private Material beam;
    private Material rafter;

    public PartsListCalculator(int carportLength, int carportWidth, int carportHeight, Material post, Material beam, Material rafter) {
        this.carportLength = carportLength;
        this.carportWidth = carportWidth;
        this.carportHeight = carportHeight;
        this.post = post;
        this.beam = beam;
        this.rafter = rafter;
    }

    public void calcCarport() {
        calcPost();
        calcBeams();
        calcRafters();
    }

    private void calcPost() {
        int amount = calcPostQuantity();
        int lengthInGround = 90;
        post.setLength(carportHeight + lengthInGround);
        double pricePrMeter = 44.0;
        double price = calcPricePrMeter(post.getLength(), pricePrMeter);
        String instruction = "Stolper nedgraves 90 cm. i jord";
        String unitType = "Stk.";

        addToPartsListItems(post.getDescription(), post.getHeight(), post.getWidth(), post.getLength(), price, amount, unitType, instruction);
    }

    public int calcPostQuantity() {
        int maxDistancePost = 340;

        return (2 * (2 + (carportLength - removeTotalLength) / maxDistancePost));
    }

    public int calcAvgDistanceBetweenPosts() {
        return (int) Math.floor((double) (carportLength - removeTotalLength) / (((double) calcPostQuantity() /2)-1));
    }

    public int calcRemainingDistance() {
        int remainingDistance = 0;
        int totalLength = (carportLength - removeTotalLength);
        int avgDistance = calcAvgDistanceBetweenPosts();

        if (totalLength % avgDistance != 0) {
            remainingDistance = totalLength - (avgDistance * (((calcPostQuantity() / 2) - 1) - 1));
        }

        return remainingDistance;
    }

    private void calcBeams() {
        int avgBeamLength = calcAvgDistanceBetweenPosts();
        int avgBeamLengthLast = avgBeamLength + removeLengthBehind;
        int amountOfDistancesWithAvg = ((calcPostQuantity()/2)-1);

        int remainingBeamLength;
        if (calcPostQuantity() == 4) {
            remainingBeamLength = avgBeamLength + removeTotalLength;
        } else if (calcPostQuantity() == 6) {
            remainingBeamLength = avgBeamLength + removeLengthFront;
        } else {
            remainingBeamLength = calcRemainingDistance() + removeLengthFront;
        }

        String instruction = "Remme i sider, sadles ned i stolper.";
        String unitType = "Stk.";

        double pricePrMeter = 44.0;
        double price = 0;
        if (calcPostQuantity() == 4) {
            price += calcPricePrMeter(remainingBeamLength, pricePrMeter);
            addBeamsToPartsListItems(beam.getDescription(), beam.getHeight(), beam.getWidth(), price, unitType, remainingBeamLength, amountOfDistancesWithAvg, instruction + " Skæres til " + remainingBeamLength + "cm.");
        } else {
            if (calcPostQuantity() > 6) { //Add middle beams
                price += calcPricePrMeter(avgBeamLength, pricePrMeter);
                addBeamsToPartsListItems(beam.getDescription(), beam.getHeight(), beam.getWidth(), pricePrMeter, unitType, avgBeamLength, amountOfDistancesWithAvg, instruction + " Midter Remme. Skæres til " + avgBeamLength + "cm.");
            }

            price += calcPricePrMeter(avgBeamLengthLast, pricePrMeter);
            price += calcPricePrMeter(remainingBeamLength, pricePrMeter);

            //Add end beams
            addBeamsToPartsListItems(beam.getDescription(), beam.getHeight(), beam.getWidth(), price, unitType, avgBeamLengthLast, 1, instruction + " Bagerste Remme. Skæres til " + avgBeamLengthLast + "cm.");
            //Add front beams
            addBeamsToPartsListItems(beam.getDescription(), beam.getHeight(), beam.getWidth(), price, unitType, remainingBeamLength, 1, instruction + " Forreste Remme. Skæres til " + remainingBeamLength + "cm.");
        }
    }

    private void addBeamsToPartsListItems(String beamDescr, int beamHeightMM, int beamWidthMM, double pricePrM, String unitType, int givenLength, int amount, String instruction) {
        Arrays.sort(lengthsOfBeamRafterPost);

        for (int beamLength: lengthsOfBeamRafterPost) {
            if (givenLength <= beamLength) {
                BigDecimal beamPrice = new BigDecimal(((double)beamLength/100.0) * pricePrM).setScale(2, RoundingMode.HALF_UP);
                addToPartsListItems(beamDescr, beamHeightMM, beamWidthMM, beamLength, beamPrice.doubleValue(), amount * 2, unitType, instruction);
                break;
            }
        }
    }

    private void  calcRafters() {
        int amountOfRafters = calcAmountOfRafters(rafter.getWidth());
        int rafterLength = calcRafterLength();
        double pricePrMeter = 38.0;
        double price = calcPricePrMeter(rafterLength, pricePrMeter);
        String instruction = "Spær, monteres på rem.";
        String unitType = "Stk.";

        if (carportWidth != rafterLength) {
            instruction += " Skæres til " + carportWidth + "cm.";
        }

        addToPartsListItems(rafter.getDescription(), rafter.getHeight(), rafter.getWidth(), rafterLength, price, amountOfRafters, unitType, instruction);
    }

    public int calcAmountOfRafters(int rafterWidthMM) {
        double spaceBetweenCM = 55.0;
        double rafterWidthCM = rafterWidthMM/10.0;

        return (int) Math.ceil(carportLength / (spaceBetweenCM + rafterWidthCM));
    }

    public int calcRafterLength() {
        int rafterLength = -1;

        for (int length: lengthsOfBeamRafterPost) {
            if (carportWidth <= length) {
                rafterLength = length;
                break;
            }
        }

        return rafterLength;
    }

    public double calcPricePrMeter(int length, double pricePrMeter){
        BigDecimal materialPrice = new BigDecimal(((double) length / 100) * pricePrMeter).setScale(2, RoundingMode.HALF_UP);

        return materialPrice.doubleValue();
    }

    private void addToPartsListItems(String description, int heightMM, int widthMM, int length, double price, int amount, String unitType, String instruction) {
        Material carportPost = new Material(description, heightMM, widthMM, length, price);
        BigDecimal totalPrice = new BigDecimal(price * (double) amount).setScale(2, RoundingMode.HALF_UP);

        partsListItems.add(new PartsListItem(carportPost, amount, unitType, instruction, totalPrice.doubleValue()));
    }

    public List<PartsListItem> getPartsListItems() {
        return partsListItems;
    }
}
