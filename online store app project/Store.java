import java.util.ArrayList;

// Store class to manage products
public class Store {
    private ArrayList<Product> products;  // List of products in the store

    // Constructor to initialize the store with an empty product list
    public Store() {
        products = new ArrayList<>();
    }

    // Add a product to the store
    public void addProduct(Product product) {
        products.add(product);
    }

    // Get the list of products in the store
    public ArrayList<Product> getProducts() {
        return products;
    }

    // Display all products in the store
    public void displayProducts() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
