package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartViewForm extends JFrame {
    private JPanel cartPanel;
    private JButton placeOrderButton, goBackButton;
    private JLabel totalAmountLabel;
    private int customerId;

    public CartViewForm(int customerId) {
        this.customerId = customerId;

        setTitle("Your Shopping Cart");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Apply Nimbus look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // Header
        JLabel header = new JLabel("Shopping Cart", SwingConstants.CENTER);
        header.setFont(new Font("Sans Serif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(45, 85, 155));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // Cart Panel
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS)); // Vertical layout
        cartPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(cartPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Total Amount Panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        totalPanel.setBackground(Color.WHITE);
        totalAmountLabel = new JLabel("Total Amount: $0.00");
        totalAmountLabel.setFont(new Font("Sans Serif", Font.BOLD, 16));
        totalPanel.add(totalAmountLabel);
        add(totalPanel, BorderLayout.SOUTH);

        // Bottom Panel with Buttons
        placeOrderButton = createButton("Place Order", new Color(40, 167, 69));
        goBackButton = createButton("Go Back", new Color(0, 123, 255));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(goBackButton);
        add(buttonPanel, BorderLayout.PAGE_END);

        // Load cart items
        loadCartItems();

        // Button actions
        placeOrderButton.addActionListener(this::placeOrder);
        goBackButton.addActionListener(e -> {
            new DisplayCategories(customerId).setVisible(true);
            dispose();
        });
    }

    private void loadCartItems() {
        double totalAmount = 0.0;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT c.CartID, p.Name, c.Quantity, c.TotalPrice " +
                             "FROM Cart c JOIN Product p ON c.ProductID = p.ProductID " +
                             "WHERE c.UniqueCartID = (SELECT UniqueCartID FROM UniqueCart WHERE CustomerID = ?)")) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            cartPanel.removeAll(); // Clear existing items

            while (rs.next()) {
                int cartId = rs.getInt("CartID");
                String productName = rs.getString("Name");
                int quantity = rs.getInt("Quantity");
                double totalPrice = rs.getDouble("TotalPrice");

                totalAmount += totalPrice;

                // Create a row for each product
                JPanel row = createCartRow(cartId, productName, quantity, totalPrice);
                cartPanel.add(row);
            }

            cartPanel.revalidate();
            cartPanel.repaint();

            // Update total amount
            totalAmountLabel.setText(String.format("Total Amount: $%.2f", totalAmount));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading cart: " + e.getMessage());
        }
    }

    private JPanel createCartRow(int cartId, String productName, int quantity, double totalPrice) {
        JPanel row = new JPanel();
        row.setLayout(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Fixed height for each row

        JLabel nameLabel = new JLabel(productName + " | Quantity: " + quantity + " | Total: $" + totalPrice);
        nameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        row.add(nameLabel, BorderLayout.CENTER);

        JButton removeButton = createButton("Remove", new Color(220, 53, 69));
        removeButton.setFont(new Font("Sans Serif", Font.PLAIN, 12));
        removeButton.addActionListener(e -> removeItem(cartId, row));
        row.add(removeButton, BorderLayout.EAST);

        return row;
    }

    private void removeItem(int cartId, JPanel row) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Cart WHERE CartID = ?")) {
            pstmt.setInt(1, cartId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                cartPanel.remove(row);
                cartPanel.revalidate();
                cartPanel.repaint();
                JOptionPane.showMessageDialog(this, "Item removed from cart.");

                // Reload cart to update total amount
                loadCartItems();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing item: " + ex.getMessage());
        }
    }

    private void placeOrder(ActionEvent e) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Fetch cart items for the customer
            String fetchCartSql = "SELECT ProductID, Quantity, TotalPrice FROM Cart WHERE UniqueCartID = (SELECT UniqueCartID FROM UniqueCart WHERE CustomerID = ?)";
            PreparedStatement fetchCartStmt = conn.prepareStatement(fetchCartSql);
            fetchCartStmt.setInt(1, customerId);
            ResultSet cartRs = fetchCartStmt.executeQuery();

            while (cartRs.next()) {
                int productId = cartRs.getInt("ProductID");
                int quantity = cartRs.getInt("Quantity");
                double totalPrice = cartRs.getDouble("TotalPrice");

                // Insert into Orders table
                String insertOrderSql = "INSERT INTO Orders (OrderID, CustomerID, ProductID, CategoryID, OrderDate, TotalAmount, Status) " +
                        "VALUES (order_seq.NEXTVAL, ?, ?, (SELECT CategoryID FROM Product WHERE ProductID = ?), SYSDATE, ?, 'Placed')";
                PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderSql);
                insertOrderStmt.setInt(1, customerId);
                insertOrderStmt.setInt(2, productId);
                insertOrderStmt.setInt(3, productId);
                insertOrderStmt.setDouble(4, totalPrice);
                insertOrderStmt.executeUpdate();
            }

            // Clear the cart after order is placed
            String clearCartSql = "DELETE FROM Cart WHERE UniqueCartID = (SELECT UniqueCartID FROM UniqueCart WHERE CustomerID = ?)";
            PreparedStatement clearCartStmt = conn.prepareStatement(clearCartSql);
            clearCartStmt.setInt(1, customerId);
            clearCartStmt.executeUpdate();

            conn.commit(); // Commit transaction
            JOptionPane.showMessageDialog(this, "Order placed successfully! Keep your amount ready.");

            // Close the cart view
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error placing order: " + ex.getMessage());
        }
    }

    private JButton createButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CartViewForm(1).setVisible(true));
    }
}
