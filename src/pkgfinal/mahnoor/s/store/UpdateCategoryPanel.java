package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateCategoryPanel extends JFrame {
    private JTextField categoryIdField, categoryNameField;
    private JButton updateButton, goBackButton;

    public UpdateCategoryPanel() {
        setTitle("Update Category");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 245)); // Light background color

        // Header
        JLabel headerLabel = new JLabel("Update Category", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(35, 45, 65)); // Navy blue header
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Padding around the form

        JLabel categoryIdLabel = new JLabel("Category ID:");
        categoryIdLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        categoryIdField = new JTextField();
        styleInputField(categoryIdField);

        JLabel categoryNameLabel = new JLabel("New Category Name:");
        categoryNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        categoryNameField = new JTextField();
        styleInputField(categoryNameField);

        formPanel.add(categoryIdLabel);
        formPanel.add(categoryIdField);
        formPanel.add(categoryNameLabel);
        formPanel.add(categoryNameField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        updateButton = createStyledButton("Update", new Color(40, 167, 69)); // Green button
        goBackButton = createStyledButton("Back", new Color(45, 45, 45)); // Dark button

        buttonPanel.add(updateButton);
        buttonPanel.add(goBackButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        updateButton.addActionListener(e -> updateCategory());
        goBackButton.addActionListener(e -> {
            new CategoryManagementMenu().setVisible(true);
            dispose();
        });
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 16));
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

    private void styleInputField(JTextField field) {
        field.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        field.setForeground(Color.BLACK); // Text color
        field.setBackground(Color.WHITE); // Background color
        field.setCaretColor(Color.BLACK); // Caret (cursor) color
        field.setPreferredSize(new Dimension(400, 30)); // Increased width
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding inside the field
        ));
    }

    private void updateCategory() {
        String categoryId = categoryIdField.getText();
        String newCategoryName = categoryNameField.getText();

        if (categoryId.isEmpty() || newCategoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields must be filled!");
            return;
        }

        String query = "UPDATE Category SET CategoryName = ? WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newCategoryName);
            stmt.setInt(2, Integer.parseInt(categoryId));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Category updated successfully!");
                new CategoryManagementMenu().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Category ID not found!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating category: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateCategoryPanel().setVisible(true));
    }
}
