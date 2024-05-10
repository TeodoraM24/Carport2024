package app.entities;

import java.util.Objects;

public class PartsListItem {
    private int partsListItemId;
    private Material material;
    private int amount;
    private String unit;
    private String instruction;

    public PartsListItem(int partsListItemId, Material material, int amount, String unit, String instruction) {
        this.partsListItemId = partsListItemId;
        this.material = material;
        this.amount = amount;
        this.unit = unit;
        this.instruction = instruction;
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
                && partsListItem.getInstruction().equals(this.getInstruction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPartsListItemId(), getMaterial(), getAmount(), getUnit(), getInstruction());
    }

    @Override
    public String toString() {
        return "PartsList{" +
                "partsListId=" + partsListItemId +
                ", material=" + material +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", instruction='" + instruction +
                '}';
    }
}
