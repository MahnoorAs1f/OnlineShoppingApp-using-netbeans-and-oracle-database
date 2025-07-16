package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductListForm extends JFrame {
    private JPanel productPanel;
    private JButton goBackButton, viewCartButton;
    private int categoryId;
    private int customerId;

    public ProductListForm(int categoryId, int customerId) {
        this.categoryId = categoryId;
        this.customerId = customerId;

        setTitle("Product List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        // Product Panel
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBackground(new Color(255, 250, 250)); // Light pink panel background

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        goBackButton = new JButton("Go Back");
        viewCartButton = new JButton("View Cart");

        // Customize buttons
        customizeButton(goBackButton, new Color(70, 130, 180), Color.WHITE); // Steel blue button
        customizeButton(viewCartButton, new Color(34, 139, 34), Color.WHITE); // Forest green button

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255));
        bottomPanel.add(goBackButton);
        bottomPanel.add(viewCartButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load Products
        loadProducts();

        // Go Back Button Action
        goBackButton.addActionListener(e -> {
            new DisplayCategories(customerId).setVisible(true);
            dispose();
        });

        // View Cart Button Action
        viewCartButton.addActionListener(e -> {
            new CartViewForm(customerId).setVisible(true);
            dispose();
        });
    }

    private void loadProducts() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT ProductID, Name, Price, Stock FROM Product WHERE CategoryID = ?")) {
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("Name");
                double price = rs.getDouble("Price");
                int stock = rs.getInt("Stock");

                JPanel productItemPanel = new JPanel();
                productItemPanel.setLayout(new BorderLayout());
                productItemPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                productItemPanel.setBackground(new Color(255, 239, 213)); // Light peach background

                JLabel productLabel = new JLabel(
                        "<html><b>" + productName + "</b><br/>Price: Rs" + price + "<br/>Stock: " + stock + "</html>");
                productLabel.setFont(new Font("Arial", Font.PLAIN, 14));

                productItemPanel.add(productLabel, BorderLayout.CENTER);

                JButton addToCartButton = new JButton("Add to Cart");
                customizeButton(addToCartButton, new Color(255, 69, 0), Color.WHITE); // Red button
                productItemPanel.add(addToCartButton, BorderLayout.EAST);

                addToCartButton.addActionListener(e -> addToCart(productId));

                productPanel.add(productItemPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void addToCart(int productId) {
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (quantityStr == null || quantityStr.trim().isEmpty()) return;

        try {
            int quantity = Integer.parseInt(quantityStr);

            try (Connection conn = DBConnection.getConnection()) {
                int uniqueCartId = getOrCreateUniqueCartId(conn, customerId);

                String checkStockSql = "SELECT Stock FROM Product WHERE ProductID = ?";
                PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSql);
                checkStockStmt.setInt(1, productId);
                ResultSet stockRs = checkStockStmt.executeQuery();

                if (stockRs.next()) {
                    int stock = stockRs.getInt("Stock");
                    if (quantity > stock) {
                        JOptionPane.showMessageDialog(this, "Insufficient stock available. Max available: " + stock);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Product not found!");
                    return;
                }

                String checkCartSql = "SELECT Quantity FROM Cart WHERE ProductID = ? AND UniqueCartID = ?";
                PreparedStatement checkCartStmt = conn.prepareStatement(checkCartSql);
                checkCartStmt.setInt(1, productId);
                checkCartStmt.setInt(2, uniqueCartId);
                ResultSet cartRs = checkCartStmt.executeQuery();

                if (cartRs.next()) {
                    int existingQuantity = cartRs.getInt("Quantity");
                    int newQuantity = existingQuantity + quantity;

                    String updateCartSql = "UPDATE Cart SET Quantity = ?, TotalPrice = (SELECT Price * ? FROM Product WHERE ProductID = ?) " +
                            "WHERE ProductID = ? AND UniqueCartID = ?";
                    PreparedStatement updateCartStmt = conn.prepareStatement(updateCartSql);
                    updateCartStmt.setInt(1, newQuantity);
                    updateCartStmt.setInt(2, newQuantity);
                    updateCartStmt.setInt(3, productId);
                    updateCartStmt.setInt(4, productId);
                    updateCartStmt.setInt(5, uniqueCartId);
                    updateCartStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Product quantity updated in the cart!");
                } else {
                    int cartId = getNextCartId(conn);

                    String insertCartSql = "INSERT INTO Cart (CartID, CustomerID, ProductID, Quantity, TotalPrice, UniqueCartID) " +
                            "VALUES (?, ?, ?, ?, (SELECT Price * ? FROM Product WHERE ProductID = ?), ?)";
                    PreparedStatement cartStmt = conn.prepareStatement(insertCartSql);
                    cartStmt.setInt(1, cartId);
                    cartStmt.setInt(2, customerId);
                    cartStmt.setInt(3, productId);
                    cartStmt.setInt(4, quantity);
                    cartStmt.setInt(5, quantity);
                    cartStmt.setInt(6, productId);
                    cartStmt.setInt(7, uniqueCartId);

                    int rows = cartStmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Product added to cart!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add product to cart.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding to cart: " + e.getMessage());
        }
    }

    private int getOrCreateUniqueCartId(Connection conn, int customerId) throws SQLException {
        String getUniqueCartSql = "SELECT UniqueCartID FROM UniqueCart WHERE CustomerID = ?";
        PreparedStatement uniqueCartStmt = conn.prepareStatement(getUniqueCartSql);
        uniqueCartStmt.setInt(1, customerId);
        ResultSet rs = uniqueCartStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("UniqueCartID");
        } else {
            int uniqueCartId = getNextUniqueCartId(conn);

            String insertUniqueCartSql = "INSERT INTO UniqueCart (UniqueCartID, CustomerID) VALUES (?, ?)";
            PreparedStatement insertCartStmt = conn.prepareStatement(insertUniqueCartSql);
            insertCartStmt.setInt(1, uniqueCartId);
            insertCartStmt.setInt(2, customerId);
            insertCartStmt.executeUpdate();

            return uniqueCartId;
        }
    }

    private int getNextCartId(Connection conn) throws SQLException {
        String getNextCartIdSql = "SELECT NVL(MAX(CartID), 0) + 1 AS NextID FROM Cart";
        PreparedStatement getNextCartIdStmt = conn.prepareStatement(getNextCartIdSql);
        ResultSet rs = getNextCartIdStmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("NextID");
        }
        return 1;
    }

    private int getNextUniqueCartId(Connection conn) throws SQLException {
        String getNextUniqueCartIdSql = "SELECT NVL(MAX(UniqueCartID), 0) + 1 AS NextID FROM UniqueCart";
        PreparedStatement getNextCartIdStmt = conn.prepareStatement(getNextUniqueCartIdSql);
        ResultSet rs = getNextCartIdStmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("NextID");
        }
        return 1;
    }

    private void customizeButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private static class CustomScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(100, 149, 237); // Cornflower blue
        }
    }
  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateCategoryPanel().setVisible(true));
    }
}

