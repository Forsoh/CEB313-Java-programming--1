import java.util.ArrayList;

// Shopping cart to hold products added by the customer
public class ShoppingCart {
    private ArrayList<Product> cart;  // List of products in the cart

    // Constructor to initialize the shopping cart
    public ShoppingCart() {
        cart = new ArrayList<>();
    }

    // Add a product to the cart
    public void addProduct(Product product) {
        cart.add(product);
    }

    // Remove a product from the cart
    public void removeProduct(Product product) {
        cart.remove(product);
    }

    // Calculate the total price of the products in the cart
    public double calculateTotal() {
        double total = 0.0;
        for (Product p : cart) {
            total += p.calculateDiscountedPrice();  // Use the discounted price
        }
        return total;
    }

    // Apply discounts to products in the cart
    public void applyDiscounts() {
        for (Product p : cart) {
            System.out.println("Discounted Price for " + p.getName() + ": $" + p.calculateDiscountedPrice());
        }
    }
}
