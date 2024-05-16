package app.entities;

import java.time.LocalDate;

public class Invoice {
    private int invoiceID;
    private int partsListID;
    private int customerID;
    private LocalDate date;



    public Invoice(int invoiceID, int customerID, LocalDate date) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
        this.date = date;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getPartsListID() {
        return partsListID;
    }

    public void setPartsListID(int partsListID) {
        this.partsListID = partsListID;
    }

    public int getUserID() {
        return customerID;
    }

    public void setUserID(int userID) {
        this.customerID = userID;
    }

    public LocalDate getLocalDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
