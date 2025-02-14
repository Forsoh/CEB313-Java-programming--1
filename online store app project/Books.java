// Books product type
public class Books extends Product {
    // Constructor to initialize a Books object
    public Books(String name, double price, int quantity) {
        super(name, price, quantity);  // Call to the superclass (Product) constructor
    }

    // Override toString method to provide custom details for Books
    @Override
    public String toString() {
        return "Books: " + super.toString();
    }
}
