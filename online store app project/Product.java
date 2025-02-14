// Base class representing a product
public class Product implements Discountable {
    private String name;      // Product name
    private double price;     // Price of the product
    private int quantity;     // Stock quantity of the product
    protected double discountPercentage; // Discount percentage

    // Constructor to initialize a product
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discountPercentage = 0;  // Initially, no discount
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Apply discount by setting the discount percentage
    @Override
    public void applyDiscount(double percentage) {
        this.discountPercentage = percentage;
    }

    // Calculate the price after applying the discount
    @Override
    public double calculateDiscountedPrice() {
        return price * (1 - discountPercentage / 100);
    }

    // Override toString method to provide product details
    @Override
    public String toString() {
        return name + " - $" + price + " (Stock: " + quantity + ", Discount: " + discountPercentage + "%)";
    }
}
