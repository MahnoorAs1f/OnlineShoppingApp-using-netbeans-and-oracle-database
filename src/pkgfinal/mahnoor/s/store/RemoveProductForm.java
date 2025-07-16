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

public class RemoveProductForm extends JFrame {
    private JTextField productIdField;
    private JButton removeButton, goBackButton;

    public RemoveProductForm() {
        setTitle("Remove Product");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 245)); // Light background color

        // Header
        JLabel header = new JLabel("Remove Product", SwingConstants.CENTER);
        header.setFont(new Font("Sans Serif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(35, 45, 65)); // Navy background
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(header, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        productIdField = new JTextField();
        productIdField.setFont(new Font("Sans Serif", Font.PLAIN, 14));
        productIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        formPanel.add(productIdLabel);
        formPanel.add(productIdField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        removeButton = createStyledButton("Remove Product", new Color(220, 53, 69)); // Red button
        goBackButton = createStyledButton("Go Back", new Color(45, 45, 45)); // Dark button

        buttonPanel.add(removeButton);
        buttonPanel.add(goBackButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        removeButton.addActionListener(e -> removeProduct());
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
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
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

    private void removeProduct() {
        String productId = productIdField.getText();

        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product ID cannot be empty!");
            return;
        }

        String query = "DELETE FROM Product WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(productId));
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Product removed successfully!");
                dispose();
                new ProductManagementMenu().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Product ID not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing product: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RemoveProductForm().setVisible(true));
    }
}
