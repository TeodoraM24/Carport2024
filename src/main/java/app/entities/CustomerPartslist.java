package app.entities;

public class CustomerPartslist {
    private String materialDescription;
    private int length;
    private int amount;
    private String unitName;
    private String description;
    private int customerId;

    public CustomerPartslist(String materialDescription, int length, int amount, String unitName, String description, int customerId) {
        this.materialDescription = materialDescription;
        this.length = length;
        this.amount = amount;
        this.unitName = unitName;
        this.description = description;
        this.customerId = customerId;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
