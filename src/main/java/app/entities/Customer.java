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

    public Customer(int customerId, String email, String password, int phoneNumber, String firstName, String lastName, String address, int zip, String role, boolean haveRequest) {
        this.customerId = customerId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zip = zip;
        this.role = role;
        this.haveRequest = haveRequest;
    }

    public Customer(String firstName, String lastName, int customerId){
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
