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

public class RemoveCategoryForm extends JFrame {
    private JTextField categoryIdField;
    private JButton removeButton, backButton;

    public RemoveCategoryForm() {
        setTitle("Remove Category");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 245)); // Light background color

        // Header
        JLabel header = new JLabel("Remove Category", SwingConstants.CENTER);
        header.setFont(new Font("Sans Serif", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(new Color(35, 45, 65)); // Navy background
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(header, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel categoryIdLabel = new JLabel("Category ID:");
        categoryIdLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        categoryIdField = new JTextField();
        categoryIdField.setFont(new Font("Sans Serif", Font.PLAIN, 14));

        formPanel.add(categoryIdLabel);
        formPanel.add(categoryIdField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        removeButton = createStyledButton("Remove", new Color(220, 53, 69)); // Red button
        backButton = createStyledButton("Back", new Color(45, 45, 45)); // Dark button

        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        removeButton.addActionListener(e -> removeCategory());
        backButton.addActionListener(e -> {
            new CategoryManagementMenu().setVisible(true);
            dispose();
        });
    }

    private JButton createStyledButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(background.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
            }
        });

        return button;
    }

    private void removeCategory() {
        int categoryId;
        try {
            categoryId = Integer.parseInt(categoryIdField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Category ID.");
            return;
        }

        // Step 1: Delete child records (Products) first
        String deleteProductsQuery = "DELETE FROM Product WHERE CategoryID = ?";
        // Step 2: Delete the category
        String deleteCategoryQuery = "DELETE FROM Category WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement deleteProductsStmt = conn.prepareStatement(deleteProductsQuery);
             PreparedStatement deleteCategoryStmt = conn.prepareStatement(deleteCategoryQuery)) {

            // Delete products in the category
            deleteProductsStmt.setInt(1, categoryId);
            deleteProductsStmt.executeUpdate();

            // Delete the category itself
            deleteCategoryStmt.setInt(1, categoryId);
            int rowsAffected = deleteCategoryStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Category and associated products removed successfully!");
                categoryIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Category ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing category: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RemoveCategoryForm().setVisible(true));
    }
}
