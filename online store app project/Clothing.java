// Clothing product type
public class Clothing extends Product {
    // Constructor to initialize a Clothing object
    public Clothing(String name, double price, int quantity) {
        super(name, price, quantity);  // Call to the superclass (Product) constructor
    }

    // Override toString method to provide custom details for Clothing
    @Override
    public String toString() {
        return "Clothing: " + super.toString();
    }
}
