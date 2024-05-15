package app.entities;

import java.util.Objects;

public class Material {

    private int materialId;
    private String description;
    private int height;
    private int width;
    private int length;
    private double price;

    public Material(int materialId, String description, int height, int width, int length, double price) {
        this.materialId = materialId;
        this.description = description;
        this.height = height;
        this.width = width;
        this.length = length;
        this.price = price;
    }

    public Material(String description, int height, int width, int length, double price) {
        this.description = description;
        this.height = height;
        this.width = width;
        this.length = length;
        this.price = price;
    }

    public Material(String description, int height, int width) {
        this.description = description;
        this.height = height;
        this.width = width;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + materialId +
                ", description='" + description + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", length=" + length +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Material)) return false;
        Material material = (Material) o;
        return getMaterialId() == material.getMaterialId() && getDescription().equals(material.getDescription()) && getHeight() == material.getHeight() && getWidth() == material.getWidth() && getLength() == material.getLength() && getPrice() == material.getPrice();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialId(), getDescription(), getHeight(), getWidth(), getLength(), getPrice());
    }
}
