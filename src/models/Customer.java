package models;

public abstract class Customer {
    private String customerId;
    private String name;
    private int age;
    private String email;
    private  String contact;
    private  String address;
    static int customerCounter;

    public Customer( String name, int age, String email, String contact, String address) {
        this.customerId = "CUS"+ String.format("%30d",customerCounter) ;
        this.name = name;
        this.age = age;
        this.email = email;
        this.contact = contact;
        this.address = address;
        customerCounter++;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static int getCustomerCounter() {
        return customerCounter;
    }

    public static void setCustomerCounter(int customerCounter) {
        Customer.customerCounter = customerCounter;
    }
    public abstract void displayCustomerDetails();
    public abstract String getCustomerType();

}
