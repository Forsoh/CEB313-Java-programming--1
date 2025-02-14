import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// GUI Class
public class OnlineStoreGUI {
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField discountField;
    private JLabel totalLabel;
    private JComboBox<String> categoryDiscountComboBox;
    private ArrayList<Product> products = new ArrayList<>();

    public OnlineStoreGUI() {
        // Frame Setup
        frame = new JFrame("Online Store");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);


        // Set custom icon for the JFrame window
        try {
            Image icon = ImageIO.read(new File("C:\\Users\\BAKER MOHAMED M.M\\Downloads\\ecom.jpeg"));  // Replace with your icon file
            frame.setIconImage(icon);  // Set the icon image
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Table Configuration
        String[] columns = {"Name", "Category", "Original Price", "Discounted Price", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (row % 2 == 0) {
                    c.setBackground(new Color(245, 245, 245)); // Light grey for even rows
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };
        loadProductsFromDatabase();
        productTable.setRowHeight(35);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(productTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Product Panel
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(6, 2, 20, 20));  // Increased spacing between components
        productPanel.setBackground(new Color(248, 249, 250)); // Light background for product panel

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"Electronics", "Clothing", "Books"});
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField priceField = new JTextField();
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField quantityField = new JTextField();
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton addButton = new JButton("Add Product");
        addButton.setBackground(new Color(28, 134, 238));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createLineBorder(Color.gray));

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String category = (String) categoryComboBox.getSelectedItem();

            // Validate input
            if (name.isEmpty() || priceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                // Insert into database
                try (Connection conn = DatabaseManager.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (name, category, original_price, price, quantity) VALUES (?, ?, ?, ?, ?)")) {

                    stmt.setString(1, name);
                    stmt.setString(2, category);
                    stmt.setDouble(3, price);
                    stmt.setDouble(4, price); // Initially, original and discounted price are the same
                    stmt.setInt(5, quantity);
                    stmt.executeUpdate();
                }

                // Create the correct product type
                Product product;
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
                    default:
                        throw new IllegalArgumentException("Unknown category: " + category);
                }

                // Add to the products list and update table
                products.add(product);
                tableModel.addRow(new Object[]{name, category, price, price, quantity});

                // Clear input fields
                nameField.setText("");
                priceField.setText("");
                quantityField.setText("");

                updateTotal();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid numeric price and quantity.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error adding product to database:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        productPanel.add(nameLabel);
        productPanel.add(nameField);
        productPanel.add(categoryLabel);
        productPanel.add(categoryComboBox);
        productPanel.add(priceLabel);
        productPanel.add(priceField);
        productPanel.add(quantityLabel);
        productPanel.add(quantityField);
        productPanel.add(new JLabel());
        productPanel.add(addButton);

        frame.add(productPanel, BorderLayout.NORTH);

        // Discount & Total Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(new Color(240, 240, 240)); // Light background for bottom panel

        JLabel discountLabel = new JLabel("Discount (%):");
        discountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        discountField = new JTextField(5);
        discountField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton applyDiscountButton = new JButton("Apply Discount");
        applyDiscountButton.setBackground(new Color(28, 134, 238));
        applyDiscountButton.setForeground(Color.WHITE);
        applyDiscountButton.setFont(new Font("Arial", Font.BOLD, 14));
        applyDiscountButton.setBorder(BorderFactory.createLineBorder(Color.gray));

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel categoryDiscountLabel = new JLabel("Select Category:");
        categoryDiscountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryDiscountComboBox = new JComboBox<>(new String[]{"Electronics", "Clothing", "Books"});
        categoryDiscountComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton clearTableButton = new JButton("Clear Table");
        clearTableButton.setBackground(Color.RED);
        clearTableButton.setForeground(Color.WHITE);
        clearTableButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearTableButton.setBorder(BorderFactory.createLineBorder(Color.gray));

// 🔹 Add the Delete Button
        JButton deleteButton = new JButton("Delete Product");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBorder(BorderFactory.createLineBorder(Color.gray));

// 🔹 Add Action Listener
        deleteButton.addActionListener(e -> deleteSelectedProduct());

// 🔹 Add all components to bottom panel
        bottomPanel.add(categoryDiscountLabel);
        bottomPanel.add(categoryDiscountComboBox);
        bottomPanel.add(discountLabel);
        bottomPanel.add(discountField);
        bottomPanel.add(applyDiscountButton);
        bottomPanel.add(clearTableButton);
        bottomPanel.add(deleteButton); // ✅ ADD DELETE BUTTON HERE
        bottomPanel.add(totalLabel);

// 🔹 Add bottom panel to frame
        frame.add(bottomPanel, BorderLayout.SOUTH);


        applyDiscountButton.addActionListener(e -> {
            double discountPercentage = Double.parseDouble(discountField.getText());
            String selectedCategory = (String) categoryDiscountComboBox.getSelectedItem();

            for (Product product : products) {
                if (product.getClass().getSimpleName().equals(selectedCategory)) {
                    product.applyDiscount(discountPercentage);
                }
            }

            updateTable();
            updateTotal();
        });

        applyDiscountButton.addActionListener(e -> {
            try {
                double discountPercentage = Double.parseDouble(discountField.getText());

                // Check if the discount is between 0 and 100
                if (discountPercentage < 0 || discountPercentage > 100) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid discount between 0 and 100.", "Invalid Discount", JOptionPane.ERROR_MESSAGE);
                } else {
                    String selectedCategory = (String) categoryDiscountComboBox.getSelectedItem();

                    for (Product product : products) {
                        if (product.getClass().getSimpleName().equals(selectedCategory)) {
                            // Apply discount to product
                            product.applyDiscount(discountPercentage);

                            // Update the discounted price in the database
                            try (Connection conn = DatabaseManager.getConnection();
                                 PreparedStatement stmt = conn.prepareStatement("UPDATE products SET discounted_price = ? WHERE name = ?")) {

                                stmt.setDouble(1, product.calculateDiscountedPrice());
                                stmt.setString(2, product.getName());
                                stmt.executeUpdate();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(frame, "Error updating discounted price in database:\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    updateTable();
                    updateTotal();
                }
            } catch (NumberFormatException ex) {
                // Handle case where the input is not a valid number
                JOptionPane.showMessageDialog(frame, "Please enter a valid numeric discount.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });


        clearTableButton.addActionListener(e -> {
            products.clear();
            tableModel.setRowCount(0);
            totalLabel.setText("Total: $0.00");
        });

        bottomPanel.add(categoryDiscountLabel);
        bottomPanel.add(categoryDiscountComboBox);
        bottomPanel.add(discountLabel);
        bottomPanel.add(discountField);
        bottomPanel.add(applyDiscountButton);
        bottomPanel.add(clearTableButton);
        bottomPanel.add(totalLabel);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadProductsFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            // Clear existing data in GUI
            products.clear();
            tableModel.setRowCount(0);

            while (rs.next()) {
                String name = rs.getString("name");
                String category = rs.getString("category");
                double originalPrice = rs.getDouble("original_price");
                double discountedPrice = rs.getDouble("discounted_price");
                if (discountedPrice == 0) {
                    discountedPrice = originalPrice;  // If no discount is applied, use the original price
                }
                int quantity = rs.getInt("quantity");

                // Create the correct product type
                Product product;
                switch (category) {
                    case "Electronics":
                        product = new Electronics(name, discountedPrice, quantity);
                        break;
                    case "Clothing":
                        product = new Clothing(name, discountedPrice, quantity);
                        break;
                    case "Books":
                        product = new Books(name, discountedPrice, quantity);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown category: " + category);
                }

                // Add product to list and table
                products.add(product);
                tableModel.addRow(new Object[]{name, category, originalPrice, discountedPrice, quantity});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading products from database:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Update Total Price Label
    private void updateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.calculateDiscountedPrice() * product.getQuantity();
        }
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    // Update Table with Discounted Prices
    private void updateTable() {
        tableModel.setRowCount(0);
        for (Product product : products) {
            tableModel.addRow(new Object[] {
                    product.getName(),
                    product.getClass().getSimpleName(),
                    product.getOriginalPrice(),
                    product.calculateDiscountedPrice(),
                    product.getQuantity()
            });
        }
    }
    // Method to delete selected product from both database and GUI
    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a product to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productName = (String) tableModel.getValueAt(selectedRow, 0); // Get product name
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete '" + productName + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE name = ?")) {

                stmt.setString(1, productName);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(frame, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.removeRow(selectedRow); // Remove from GUI
                    products.removeIf(p -> p.getName().equals(productName)); // Remove from list
                    updateTotal(); // Refresh total price
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error deleting product:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static void main(String[] args) {
        try (Connection conn = DatabaseManager.getConnection()) {
            System.out.println("Database Connected Successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new OnlineStoreGUI();
    }

}
