package models;

public class PremiumCustomer extends Customer {
    private double minimumBalance;
    public PremiumCustomer( String name, int age, String contact, String address) {
        super( name, age, contact, address);
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
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
        System.out.println("Premium benefits; " + "Premium customers don't pay monthly fees");
    }

    @Override
    public String getCustomerType() {
        return "Premium";
    }

    boolean hasWaivedFees (){
        return true;
    }
}
