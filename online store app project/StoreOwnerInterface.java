import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GUI for the store owner to add products
public class StoreOwnerInterface extends JFrame {
    private Store store;  // Store object to add products to

    private JTextField nameField;      // Field to enter the product name
    private JTextField priceField;     // Field to enter the product price
    private JTextField quantityField; // Field to enter the product quantity
    private JComboBox<String> categoryComboBox;  // ComboBox for selecting product category

    // Constructor to initialize the store owner interface
    public StoreOwnerInterface(Store store) {
        this.store = store;
        setTitle("Store Owner Interface");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Product Name:");
        nameField = new JTextField(20);

        JLabel priceLabel = new JLabel("Product Price:");
        priceField = new JTextField(10);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(5);

        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>(new String[]{"Electronics", "Clothing", "Books"});

        JButton addButton = new JButton("Add Product");

        add(nameLabel);
        add(nameField);
        add(priceLabel);
        add(priceField);
        add(quantityLabel);
        add(quantityField);
        add(categoryLabel);
        add(categoryComboBox);
        add(addButton);

        // Action listener for adding a product
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String category = (String) categoryComboBox.getSelectedItem();

                Product product = null;

                // Create product based on category
                switch (category) {
                    case "Electronics":
                        product = new Electronics(name, price, quantity);
                        break;
                    case "Clothing":
                        product = new Clothing(name, price, quantity);
                        break;
                    case "Books":
                        product = new Books(name, price, quantity);
                        break;
                }

                if (product != null) {
                    store.addProduct(product);  // Add product to store
                    JOptionPane.showMessageDialog(null, "Product added successfully!");
                }
            }
        });
    }
}
