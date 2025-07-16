/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkgfinal.mahnoor.s.store;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProductForm extends JFrame {
    private JTextField productNameField, priceField, stockField, categoryIdField;
    private JButton addButton, goBackButton;

    public AddProductForm() {
        setTitle("Add Product");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 245)); // Light background color

        // Header
        JLabel headerLabel = new JLabel("Add Product", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(35, 45, 65)); // Navy header background
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel productNameLabel = new JLabel("Product Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel stockLabel = new JLabel("Stock:");
        JLabel categoryIdLabel = new JLabel("Category ID:");

        styleLabel(productNameLabel);
        styleLabel(priceLabel);
        styleLabel(stockLabel);
        styleLabel(categoryIdLabel);

        productNameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        categoryIdField = new JTextField();

        styleInputField(productNameField);
        styleInputField(priceField);
        styleInputField(stockField);
        styleInputField(categoryIdField);

        formPanel.add(productNameLabel);
        formPanel.add(productNameField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(stockLabel);
        formPanel.add(stockField);
        formPanel.add(categoryIdLabel);
        formPanel.add(categoryIdField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        addButton = createStyledButton("Add Product", new Color(40, 167, 69)); // Green button
        goBackButton = createStyledButton("Go Back", new Color(45, 45, 45)); // Dark button

        buttonPanel.add(addButton);
        buttonPanel.add(goBackButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addProduct());
        goBackButton.addActionListener(e -> {
            new ProductManagementMenu().setVisible(true);
            dispose();
        });
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Sans Serif", Font.PLAIN, 16));
    }

    private void styleInputField(JTextField field) {
        field.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(Color.WHITE);
    }

    private void addProduct() {
        String productName = productNameField.getText();
        String price = priceField.getText();
        String stock = stockField.getText();
        String categoryId = categoryIdField.getText();

        if (productName.isEmpty() || price.isEmpty() || stock.isEmpty() || categoryId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        String query = "INSERT INTO Product (ProductID, Name, Price, Stock, CategoryID) VALUES (product_seq.NEXTVAL, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productName);
            stmt.setDouble(2, Double.parseDouble(price));
            stmt.setInt(3, Integer.parseInt(stock));
            stmt.setInt(4, Integer.parseInt(categoryId));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            dispose();
            new ProductManagementMenu().setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddProductForm().setVisible(true));
    }
}
