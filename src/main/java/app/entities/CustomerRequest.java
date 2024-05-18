package app.entities;

import java.time.LocalDate;
import java.util.Objects;

public class CustomerRequest {
    private int customerRequestId;
    private int requestLength;
    private int requestWidth;
    private int requestHeight;
    private String requestTileType;
    private LocalDate date;
    private String status;
    private Customer customer;

    public CustomerRequest(int customerRequestId, int requestLength, int requestWidth, int requestHeight, String requestTileType, LocalDate date, String status) {
        this.customerRequestId = customerRequestId;
        this.requestLength = requestLength;
        this.requestWidth = requestWidth;
        this.requestHeight = requestHeight;
        this.requestTileType = requestTileType;
        this.date = date;
        this.status = status;
    }

    public CustomerRequest(int customerRequestId, int requestLength, int requestWidth, int requestHeight, String requestTileType, LocalDate date, String status, Customer customer) {
        this.customerRequestId = customerRequestId;
        this.requestLength = requestLength;
        this.requestHeight = requestHeight;
        this.requestWidth = requestWidth;
        this.date = date;
        this.status = status;
        this.requestTileType = requestTileType;
        this.customer = customer;
    }

    public int getCustomerRequestId() {
        return customerRequestId;
    }

    public void setCustomerRequestId(int customerRequestId) {
        this.customerRequestId = customerRequestId;
    }

    public int getRequestLength() {
        return requestLength;
    }

    public void setRequestLength(int requestLength) {
        this.requestLength = requestLength;
    }

    public int getRequestWidth() {
        return requestWidth;
    }

    public void setRequestWidth(int requestWidth) {
        this.requestWidth = requestWidth;
    }

    public int getRequestHeight() {
        return requestHeight;
    }

    public void setRequestHeight(int requestHeight) {
        this.requestHeight = requestHeight;
    }

    public String getRequestTileType() {
        return requestTileType;
    }

    public void setRequestTileType(String requestTileType) {
        this.requestTileType = requestTileType;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof CustomerRequest request))
            return false;
        return request.getCustomerRequestId() == this.getCustomerRequestId()
                && request.getRequestLength() == this.getRequestLength()
                && request.getRequestHeight() == this.getRequestHeight()
                && request.getRequestWidth() == this.getRequestWidth()
                && request.getRequestTileType().equals(this.getRequestTileType())
                && request.getDate() == this.getDate()
                && request.getStatus().equals(this.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerRequestId(), getRequestLength(), getRequestWidth(), getRequestHeight(), getRequestTileType(), getDate(), getStatus());
    }

    @Override
    public String toString() {
        return "CustomerRequest{" +
                "customerRequestId=" + customerRequestId +
                ", requestLength=" + requestLength +
                ", requestWidth=" + requestWidth +
                ", requestHeight=" + requestHeight +
                ", requestTileType='" + requestTileType + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}