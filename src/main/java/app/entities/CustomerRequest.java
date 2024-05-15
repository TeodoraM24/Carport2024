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
    Customer customer;

    public CustomerRequest(int customerRequestId, int length, int height, int width, String tileType, LocalDate date, String status) {
        this.customerRequestId = customerRequestId;
        this.length = length;
        this.height = height;
        this.width = width;
        this.date = date;
        this.status = status;
        this.tileType = tileType;
    }

    public CustomerRequest(int customerRequestId, int length, int height, int width, String tileType, LocalDate date, String status, Customer customer) {
        this.customerRequestId = customerRequestId;
        this.length = length;
        this.height = height;
        this.width = width;
        this.date = date;
        this.status = status;
        this.tileType = tileType;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerRequest{" +
                "customerRequestId=" + customerRequestId +
                ", length=" + length +
                ", height=" + height +
                ", width=" + width +
                ", tileType='" + tileType + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
