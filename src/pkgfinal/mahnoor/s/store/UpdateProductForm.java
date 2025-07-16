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

/**
 *
 * @author PMYLS
 */
public class UpdateProductForm extends JFrame {
    private JTextField productIdField, nameField, priceField, stockField, categoryIdField;
    private JButton updateButton, goBackButton;

    public UpdateProductForm() {
        setTitle("Update Product");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout(10, 10));

        // Main Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(255, 255, 255));

        // Labels and Fields
        formPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        formPanel.add(productIdField);

        formPanel.add(new JLabel("New Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("New Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("New Stock:"));
        stockField = new JTextField();
        formPanel.add(stockField);

        formPanel.add(new JLabel("New Category ID:"));
        categoryIdField = new JTextField();
        formPanel.add(categoryIdField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(255, 255, 255));

        updateButton = new JButton("Update Product");
        updateButton.setBackground(new Color(34, 139, 34)); // Green button
        updateButton.setFont(new Font("Sans Serif", Font.BOLD, 14));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        updateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        goBackButton = new JButton("Go Back");
        goBackButton.setBackground(new Color(45, 45, 45)); // Dark button
        goBackButton.setFont(new Font("Sans Serif", Font.BOLD, 14));
        goBackButton.setForeground(Color.WHITE);
        goBackButton.setFocusPainted(false);
        goBackButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        goBackButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        buttonPanel.add(updateButton);
        buttonPanel.add(goBackButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        updateButton.addActionListener(e -> updateProduct());
        goBackButton.addActionListener(e -> {
            new ProductManagementMenu().setVisible(true);
            dispose();
        });
    }

    private void updateProduct() {
        String productId = productIdField.getText();
        String name = nameField.getText();
        String price = priceField.getText();
        String stock = stockField.getText();
        String categoryId = categoryIdField.getText();

        if (productId.isEmpty() || name.isEmpty() || price.isEmpty() || stock.isEmpty() || categoryId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!");
            return;
        }

        String query = "UPDATE Product SET Name = ?, Price = ?, Stock = ?, CategoryID = ? WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(price));
            stmt.setInt(3, Integer.parseInt(stock));
            stmt.setInt(4, Integer.parseInt(categoryId));
            stmt.setInt(5, Integer.parseInt(productId));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                dispose();
                new ProductManagementMenu().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Product ID not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateProductForm().setVisible(true));
    }
}