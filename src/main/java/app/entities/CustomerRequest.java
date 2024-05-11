package app.entities;

import java.time.LocalDate;

public class CustomerRequest {
    int customerRequestId;
    int length;
    int height;
    int width;
    String tileType;
    LocalDate date;
    String status;

    public CustomerRequest(int customerRequestId, LocalDate date, int length, int height, int width, String status) {
        this.customerRequestId = customerRequestId;
        this.date = date;
        this.length = length;
        this.height = height;
        this.width = width;
        this.status = status;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
