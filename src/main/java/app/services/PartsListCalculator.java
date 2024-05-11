package app.services;

import app.entities.Material;
import app.entities.PartsListItem;

import java.util.*;

public class PartsListCalculator {
    private List<PartsListItem> partsListItems = new ArrayList<>();
    private int width;
    private int length;
    private int height;
    private Integer[] beamsRaftersLengths = {300, 360, 420, 480, 540, 600};
    private int maxDistancePost = 340;
    private int removeDistanceLength = 130;

    public PartsListCalculator(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public void calcCarport() {
        calcPost();
        calcBeams();
        calcRafters();
    }

    private void calcPost() {
        int amount = calcPostQuantity();
        int totalLength = height + 90;
        int pricePrMeter = 44;
        int totalPrice = (totalLength / 100) * pricePrMeter; // change price to double?
        int postHeightMM = 97;
        int postWidthMM = 97;
        String postDescr = postHeightMM + "x" + postWidthMM + " mm. trykimp. Stolpe";
        String instruction = "Stolper nedgraves 90 cm. i jord";
        String unitType = "Stk.";
        addToPartsListItems(postDescr, postHeightMM, postWidthMM, totalLength, totalPrice, amount, unitType, instruction);
    }

    private void addToPartsListItems(String description, int heightMM, int widthMM, int length, int price, int amount, String unitType, String instruction) {
        Material carportPost = new Material(description, heightMM, widthMM, length, price);
        partsListItems.add(new PartsListItem(carportPost, amount, unitType, instruction));
    }

    public int calcPostQuantity() {
        return (2 * (2 + (length - removeDistanceLength) / maxDistancePost));
    }

    public int calcAvgDistanceBetweenPosts() {
        return (int) Math.floor((double) (length - removeDistanceLength) / (((double) calcPostQuantity() /2)-1));
    }

    public int calcRemainingDistance() {
        int remainingDistance = 0;
        int totalLength = (length - removeDistanceLength);
        int avgDistance = calcAvgDistanceBetweenPosts();
        if (totalLength % avgDistance != 0) {
            remainingDistance = totalLength - (avgDistance * (((calcPostQuantity() / 2) - 1) - 1));
        }
        return remainingDistance;
    }

    private void calcBeams() {
        int avgBeamLength = calcAvgDistanceBetweenPosts();
        int avgBeamLengthLast = avgBeamLength + 30;
        int amountOfDistancesWithAvg = ((calcPostQuantity()/2)-1);

        int remainingBeamLength;
        if (calcPostQuantity() == 4) {
            remainingBeamLength = avgBeamLength + 130;
        } else if (calcPostQuantity() == 6) {
            remainingBeamLength = avgBeamLength + 100;
        } else {
            remainingBeamLength = calcRemainingDistance() + 100;
        }

        int pricePrMeter = 38;
        int beamHeightMM = 45;
        int beamWidthMM = 195;
        String beamDescr = beamHeightMM + "x" + beamWidthMM + " mm. spærtræ ubh.";
        String instruction = "Remme i sider, sadles ned i stolper.";
        String unitType = "Stk.";

        if (calcPostQuantity() == 4) {
            addBeamsToPartsListItems(beamDescr, beamHeightMM, beamWidthMM, pricePrMeter, unitType, remainingBeamLength, amountOfDistancesWithAvg, instruction + " Skæres til " + remainingBeamLength + "cm.");
        } else {
            if (calcPostQuantity() > 6) {
                //Add middle beams
                addBeamsToPartsListItems(beamDescr, beamHeightMM, beamWidthMM, pricePrMeter, unitType, avgBeamLength, amountOfDistancesWithAvg, instruction + " Midter Remme. Skæres til " + avgBeamLength + "cm.");
            }

            //Add end beams
            addBeamsToPartsListItems(beamDescr, beamHeightMM, beamWidthMM, pricePrMeter, unitType, avgBeamLengthLast, 1, instruction + " Bagerste Remme. Skæres til " + avgBeamLengthLast + "cm.");
            //Add front beams
            addBeamsToPartsListItems(beamDescr, beamHeightMM, beamWidthMM, pricePrMeter, unitType, remainingBeamLength, 1, instruction + " Forreste Remme. Skæres til " + remainingBeamLength + "cm.");
        }
    }

    private void addBeamsToPartsListItems(String beamDescr, int beamHeightMM, int beamWidthMM, int pricePrM, String unitType, int givenLength, int amount, String instruction) {
        Arrays.sort(beamsRaftersLengths);
        for (int beamLength: beamsRaftersLengths) {
            if (givenLength <= beamLength) {
                int price = (beamLength/100) * pricePrM; // change price to double?
                addToPartsListItems(beamDescr, beamHeightMM, beamWidthMM, beamLength, price, amount * 2, unitType, instruction);
                break;
            }
        }
    }

    //spær
    private void  calcRafters() {

    }

    public List<PartsListItem> getPartsListItems() {
        return partsListItems;
    }
}
