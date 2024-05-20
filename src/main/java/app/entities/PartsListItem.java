package app.entities;

import java.util.Objects;

public class PartsListItem {
    private int partsListItemId;
    private Material material;
    private int amount;
    private String unit;
    private String instruction;
    private double totalPrice;

    public PartsListItem(Material material, int amount, String unit, String instruction, double totalPrice) {
        this.material = material;
        this.amount = amount;
        this.unit = unit;
        this.instruction = instruction;
        this.totalPrice = totalPrice;
    }

    public int getPartsListItemId() {
        return partsListItemId;
    }

    public void setPartsListItemId(int partsListItemId) {
        this.partsListItemId = partsListItemId;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof PartsListItem partsListItem))
            return false;
        return partsListItem.getPartsListItemId() == this.getPartsListItemId()
                && partsListItem.getMaterial().equals(this.getMaterial())
                && partsListItem.getAmount() == this.getAmount()
                && partsListItem.getUnit().equals(this.getUnit())
                && partsListItem.getInstruction().equals(this.getInstruction())
                && partsListItem.getTotalPrice() == this.getTotalPrice();
    }


    @Override
    public int hashCode() {
        return Objects.hash(getPartsListItemId(), getMaterial(), getAmount(), getUnit(), getInstruction(), getTotalPrice());
    }

    @Override
    public String toString() {
        return "PartsListItem{" +
                "partsListItemId=" + partsListItemId +
                ", material=" + material +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", instruction='" + instruction + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
