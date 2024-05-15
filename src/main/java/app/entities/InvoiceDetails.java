package app.entities;

public class InvoiceDetails {
    private String materialDescription;
    private int length;
    private int amount;
    private String unit;
    private String instructionDescription;
    private int customerId;
    private int invoiceId;

    public InvoiceDetails(String materialDescription, int length, int amount, String unit, String instructionDescription, int customerId, int invoiceId) {
        this.materialDescription = materialDescription;
        this.length = length;
        this.amount = amount;
        this.unit = unit;
        this.instructionDescription = instructionDescription;
        this.customerId = customerId;
        this.invoiceId = invoiceId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getInstructionDescription() {
        return instructionDescription;
    }

    public void setInstructionDescription(String instructionDescription) {
        this.instructionDescription = instructionDescription;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
}
