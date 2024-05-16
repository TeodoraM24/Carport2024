package app.entities;

public class Offer {
    private int offerId;
    private String carportSize;
    private String toolShedSize;
    private String claddingDesc;
    private String rafterTypeDesc;
    private String supportBeamDescSize;
    private String roofMaterials;
    private double totalPriceWithTax;

    public Offer(int offerId, String carportSize, String toolShedSize, String clddingDesc, String rafterTypeDesc, String supportBeamDescSize, String roofMaterials, double totalPriceWithTax) {
        this.offerId = offerId;
        this.carportSize = carportSize;
        this.toolShedSize = toolShedSize;
        this.claddingDesc = clddingDesc;
        this.rafterTypeDesc = rafterTypeDesc;
        this.supportBeamDescSize = supportBeamDescSize;
        this.roofMaterials = roofMaterials;
        this.totalPriceWithTax = totalPriceWithTax;
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

    public String getToolShedSize() {
        return toolShedSize;
    }

    public void setToolShedSize(String toolShedSize) {
        this.toolShedSize = toolShedSize;
    }

    public String getCladdingDesc() {
        return claddingDesc;
    }

    public void setCladdingDesc(String claddingDesc) {
        this.claddingDesc = claddingDesc;
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


    @Override
    public String toString() {
        return "Offer{" +
                "offerId=" + offerId +
                ", carportSize='" + carportSize + '\'' +
                ", toolShedSize='" + toolShedSize + '\'' +
                ", clddingDesc='" + claddingDesc + '\'' +
                ", rafterTypeDesc='" + rafterTypeDesc + '\'' +
                ", supportBeamDescSize='" + supportBeamDescSize + '\'' +
                ", roofMaterials='" + roofMaterials + '\'' +
                ", totalPriceWithTax=" + totalPriceWithTax +
                '}';
    }
}
