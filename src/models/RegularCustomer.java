package models;

public class RegularCustomer extends Customer {
    public RegularCustomer(String name, int age, String contact, String address) {
        super( name, age, contact, address);
    }

    @Override
    public void displayCustomerDetails() {
        System.out.println("models.Customer details");
        System.out.println("____________________");
        System.out.println("models.Customer number: " + getCustomerId());
        System.out.println("models.Customer holder name: " + getName());
        System.out.println("models.Customer age: " + getAge());
        System.out.println("models.Customer contacts: " + getContact());
        System.out.println("models.Customer Address: " + getAddress() );
    }

    @Override
    public String getCustomerType() {
        return "Regular";
    }
}
