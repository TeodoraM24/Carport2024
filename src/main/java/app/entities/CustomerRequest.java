package app.entities;

import java.time.*;

public class CustomerRequest {

    int customerRequestId;
    int length;
    int height;
    int width;
    String tileType;
    String status;
    LocalDate date;

    public CustomerRequest(int customerRequestId, int length, int height, int width, String tileType, String status, LocalDate date) {
        this.customerRequestId = customerRequestId;
        this.length = length;
        this.height = height;
        this.width = width;
        this.tileType = tileType;
        this.status = status;
        this.date = date;
    }

    public int getCustomerRequestId() {
        return customerRequestId;
    }

    public void setCustomerRequestId(int customerRequestId) {
        this.customerRequestId = customerRequestId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

    public String getTileType() {
        return tileType;
    }

    public void setTileType(String tileType) {
        this.tileType = tileType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}