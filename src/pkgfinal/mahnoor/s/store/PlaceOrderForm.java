/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkgfinal.mahnoor.s.store;


import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PlaceOrderForm extends JFrame {
    private int customerId;
    private JButton placeOrderButton, goBackButton;

    public PlaceOrderForm(int customerId) {
        this.customerId = customerId;

        setTitle("Place Order");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 10, 10));
        getContentPane().setBackground(new Color(230, 218, 225));

        // Place Order Button
        placeOrderButton = new JButton("Place Order");
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 18));
        placeOrderButton.setBackground(new Color(72, 201, 176));
        placeOrderButton.setForeground(Color.WHITE);

        // Go Back Button
        goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.BOLD, 18));
        goBackButton.setBackground(new Color(115, 130, 237));
        goBackButton.setForeground(Color.WHITE);

        add(placeOrderButton);
        add(goBackButton);

        // Button Actions
        placeOrderButton.addActionListener(e -> placeOrder());
        goBackButton.addActionListener(e -> {
            new CartViewForm(customerId).setVisible(true);
            dispose();
        });
    }

  private void placeOrder() {
    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false); // Enable transaction

        // Fetch Cart Items
        String fetchCartSql = "SELECT ProductID, Quantity, TotalPrice FROM Cart WHERE CustomerID = ?";
        PreparedStatement fetchCartStmt = conn.prepareStatement(fetchCartSql);
        fetchCartStmt.setInt(1, customerId);
        ResultSet cartRs = fetchCartStmt.executeQuery();

        boolean hasItems = false;
        while (cartRs.next()) {
            hasItems = true;
            int productId = cartRs.getInt("ProductID");
            int quantity = cartRs.getInt("Quantity");
            double totalPrice = cartRs.getDouble("TotalPrice");

            // Get the next OrderID
            String getNextOrderIdSql = "SELECT order_seq.NEXTVAL AS NextOrderID FROM dual";
            PreparedStatement getNextOrderIdStmt = conn.prepareStatement(getNextOrderIdSql);
            ResultSet orderIdRs = getNextOrderIdStmt.executeQuery();
            int nextOrderId = 0;
            if (orderIdRs.next()) {
                nextOrderId = orderIdRs.getInt("NextOrderID");
            }

            // Insert into Orders
            String insertOrderSql = "INSERT INTO Orders (OrderID, CustomerID, ProductID, CategoryID, OrderDate, TotalAmount, Status) " +
                    "VALUES (?, ?, ?, (SELECT CategoryID FROM Product WHERE ProductID = ?), SYSDATE, ?, 'Placed')";
            PreparedStatement insertOrderStmt = conn.prepareStatement(insertOrderSql);
            insertOrderStmt.setInt(1, nextOrderId);
            insertOrderStmt.setInt(2, customerId);
            insertOrderStmt.setInt(3, productId);
            insertOrderStmt.setInt(4, productId);
            insertOrderStmt.setDouble(5, totalPrice);
            insertOrderStmt.executeUpdate();

            // Decrease product stock
            String updateStockSql = "UPDATE Product SET Stock = Stock - ? WHERE ProductID = ?";
            PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSql);
            updateStockStmt.setInt(1, quantity);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();
        }

        if (hasItems) {
            // Clear the Cart
            String clearCartSql = "DELETE FROM Cart WHERE CustomerID = ?";
            PreparedStatement clearCartStmt = conn.prepareStatement(clearCartSql);
            clearCartStmt.setInt(1, customerId);
            clearCartStmt.executeUpdate();

            conn.commit(); // Commit transaction

            // Show success message and transition to OrderHistoryForm
            JOptionPane.showMessageDialog(this, "Order placed successfully! Keep your amount ready.");
            //new OrderHistoryForm(customerId).setVisible(true); // Open order history
            dispose(); // Close the current form
        } else {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Please add items to place an order.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error placing order: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlaceOrderForm(1).setVisible(true)); // Test with customerId = 1
    }
}
