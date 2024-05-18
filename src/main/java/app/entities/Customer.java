package app.entities;

public class Customer {
    private int customerId;
    private String email;
    private String password;
    private int phoneNumber;
    private String firstName;
    private String lastName;
    private String address;
    private int zip;
    private String role;
    private boolean haveRequest;
    private int customer_request_id;
    private int offer_id;

    public Customer(int customerId, String email, String password, int phoneNumber, String firstName, String lastName, String address, int zip, String role, int customer_request_id) {
        this.customerId = customerId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zip = zip;
        this.role = role;
        this.customer_request_id = customer_request_id;
    }

    public Customer(int customerId, String email, String password, int phoneNumber, String firstName, String lastName, String address, int zip, String role) {
        this.customerId = customerId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zip = zip;
        this.role = role;
    }

    public Customer(int customerId, String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isHaveRequest() {
        return haveRequest;
    }

    public void setHaveRequest(boolean haveRequest) {
        this.haveRequest = haveRequest;
    }

    public int getCustomer_request_id() {
        return customer_request_id;
    }

    public void setCustomer_request_id(int customer_request_id) {
        this.customer_request_id = customer_request_id;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "customerId=" + customerId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", zip=" + zip +
                ", role='" + role + '\'' +
                ", haveRequest=" + haveRequest +
                '}';
    }

}
