// Electronics product type
public class Electronics extends Product {
    // Constructor to initialize an Electronics object
    public Electronics(String name, double price, int quantity) {
        super(name, price, quantity);  // Call to the superclass (Product) constructor
    }

    // Override toString method to provide custom details for Electronics
    @Override
    public String toString() {
        return "Electronics: " + super.toString();
    }
}
