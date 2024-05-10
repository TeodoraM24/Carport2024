package app.services;

import app.entities.PartsListItem;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;
import app.persistence.PartsListItemMapper;

import java.util.ArrayList;
import java.util.List;

public class MaterialsCalculator {
    private List<PartsListItem> partsListItems = new ArrayList<>();
    private int width;
    private int length;
    private int height;
    private ConnectionPool connectionPool;

    public MaterialsCalculator(int length, int width, int height, ConnectionPool connectionPool) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.connectionPool = connectionPool;
    }

    public void calcCarport() throws DatabaseException {
        calcPost();
        calcBeams();
        calcRafters();
    }

    //stolper
    private void calcPost() throws DatabaseException {
        //antal stolper
        int amount = calcPostQuantity(); //change

        //længde på stolper
        int burialLength = 90;
        int totalHeight = height + burialLength;

        //pris
        int pricePrMeter = 35;
        int totalPrice = (totalHeight / 100) * pricePrMeter; // change price to double?

        //beskrivelse
        int postLengthMM = 97;
        int postWidthMM = 97;
        String postDesc = postLengthMM + "x" + postWidthMM + " mm. trykimp. Stolpe";

        //check if its in db and add if not
        int postId = MaterialMapper.getMaterialIdByData(postDesc, totalHeight, postWidthMM, postLengthMM, totalPrice, connectionPool);
        if (postId == -1) {
            MaterialMapper.addMaterial(postDesc, totalHeight, width, length, totalPrice, connectionPool);
            postId = MaterialMapper.getMaterialIdByData(postDesc, totalHeight, postWidthMM, postLengthMM, totalPrice, connectionPool);
        }

        //Maybe dont save to db before sending offer?

        String instruction = "Stolper nedgraves 90 cm. i jord";
        int unitId = 1;
        PartsListItemMapper.addPartsListItem(postId, amount, instruction, unitId, connectionPool);
        int partsListItemId = PartsListItemMapper.getPartsListItemIdByData(postId, amount, instruction, unitId, connectionPool);


        //create partslist
        //get partslistid
        //use partslistitemid and partslistid to insert into fk-table
    }

    public int calcPostQuantity() {
        int maxDistance = 340;
        int distanceFront = 100;
        int distanceBack = 30;

        return (2 * (2 + (length - distanceFront - distanceBack) /maxDistance));
    }

    //remme
    private void calcBeams() {

    }

    //spær
    private void  calcRafters() {

    }

    public List<PartsListItem> getPartsListItems() {
        return partsListItems;
    }
}
