package app.entities;

public class Offer {
    private int offerId;
    private String carportSize;
    private String rafterTypeDesc;
    private String supportBeamDescSize;
    private String roofMaterials;
    private double totalPriceWithTax;
    private String status;
    private int partsListId;
    private int priceId;
    private int customerRequestId;

    public Offer(int offerId, String carportSize, String rafterTypeDesc, String supportBeamDescSize, String roofMaterials, double totalPriceWithTax, String status, int partsListId, int priceId, int customerRequestId) {
        this.offerId = offerId;
        this.carportSize = carportSize;
        this.rafterTypeDesc = rafterTypeDesc;
        this.supportBeamDescSize = supportBeamDescSize;
        this.roofMaterials = roofMaterials;
        this.totalPriceWithTax = totalPriceWithTax;
        this.status = status;
        this.partsListId = partsListId;
        this.priceId = priceId;
        this.customerRequestId = customerRequestId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getCarportSize() {
        return carportSize;
    }

    public void setCarportSize(String carportSize) {
        this.carportSize = carportSize;
    }

    public String getRafterTypeDesc() {
        return rafterTypeDesc;
    }

    public void setRafterTypeDesc(String rafterTypeDesc) {
        this.rafterTypeDesc = rafterTypeDesc;
    }

    public String getSupportBeamDescSize() {
        return supportBeamDescSize;
    }

    public void setSupportBeamDescSize(String supportBeamDescSize) {
        this.supportBeamDescSize = supportBeamDescSize;
    }

    public String getRoofMaterials() {
        return roofMaterials;
    }

    public void setRoofMaterials(String roofMaterials) {
        this.roofMaterials = roofMaterials;
    }

    public double getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public void setTotalPriceWithTax(double totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPartsListId() {
        return partsListId;
    }

    public void setPartsListId(int partsListId) {
        this.partsListId = partsListId;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getCustomerRequestId() {
        return customerRequestId;
    }

    public void setCustomerRequestId(int customerRequestId) {
        this.customerRequestId = customerRequestId;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "offerId=" + offerId +
                ", carportSize='" + carportSize + '\'' +
                ", rafterTypeDesc='" + rafterTypeDesc + '\'' +
                ", supportBeamDescSize='" + supportBeamDescSize + '\'' +
                ", roofMaterials='" + roofMaterials + '\'' +
                ", totalPriceWithTax=" + totalPriceWithTax +
                ", status='" + status + '\'' +
                '}';
    }
}
