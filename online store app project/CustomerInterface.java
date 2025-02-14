import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GUI for customers to view and buy products
public class CustomerInterface extends JFrame {
    private Store store;
    private ShoppingCart shoppingCart;

    // Constructor to initialize customer interface
    public CustomerInterface(Store store, ShoppingCart shoppingCart) {
        this.store = store;
        this.shoppingCart = shoppingCart;

        setTitle("Customer Shopping Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout());

        JButton viewButton = new JButton("View Products");
        JButton addButton = new JButton("Add to Cart");
        JButton checkoutButton = new JButton("Checkout");

        JTextArea productDisplay = new JTextArea(10, 40);
        productDisplay.setEditable(false);

        add(viewButton);
        add(new JScrollPane(productDisplay));
        add(addButton);
        add(checkoutButton);

        // Action listener to view products
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productDisplay.setText("");  // Clear the display area
                for (Product product : store.getProducts()) {
                    productDisplay.append(product + "\n");  // Display products
                }
            }
        });

        // Action listener to add a product to the cart
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = JOptionPane.showInputDialog("Enter product name:");
                Product selectedProduct = null;
                // Search for the selected product in the store
                for (Product p : store.getProducts()) {
                    if (p.getName().equalsIgnoreCase(productName) && p.getQuantity() > 0) {
                        selectedProduct = p;
                        shoppingCart.addProduct(selectedProduct);  // Add to cart
                        p.setQuantity(p.getQuantity() - 1);  // Decrement stock
                        break;
                    }
                }

                if (selectedProduct != null) {
                    JOptionPane.showMessageDialog(null, selectedProduct.getName() + " added to cart.");
                } else {
                    JOptionPane.showMessageDialog(null, "Product not available.");
                }
            }
        });

        // Action listener for checkout
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double total = shoppingCart.calculateTotal();
                shoppingCart.applyDiscounts();  // Apply discounts
                JOptionPane.showMessageDialog(null, "Total: $" + total);
            }
        });
    }
}
