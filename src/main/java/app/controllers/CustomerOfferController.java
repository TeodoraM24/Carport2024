package app.controllers;

public class CustomerOfferController {

    public static void calculateOffer() {
        //Create Parts list
 /*//check if its in db and add if not
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

*/
        //create partslist
        //get partslistid
        //use partslistitemid and partslistid to insert into fk-table
    }
}
