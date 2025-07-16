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

public class AddCategoryForm extends JFrame {
    private JTextField categoryNameField;
    private JButton addButton, backButton;

    public AddCategoryForm() {
        setTitle("Add Category");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 245)); // Light background color

        // Header
        JLabel header = new JLabel("Add Category", SwingConstants.CENTER);
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

        JLabel categoryLabel = new JLabel("Category Name:");
        categoryLabel.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        categoryNameField = new JTextField();
        categoryNameField.setFont(new Font("Sans Serif", Font.PLAIN, 14));

        formPanel.add(categoryLabel);
        formPanel.add(categoryNameField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        addButton = createStyledButton("Add", new Color(40, 167, 69)); // Green button
        backButton = createStyledButton("Back", new Color(45, 45, 45)); // Dark button

        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addCategory());
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

    private void addCategory() {
        String categoryName = categoryNameField.getText().trim();
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.");
            return;
        }

        String query = "INSERT INTO Category (CategoryID, CategoryName) VALUES (Category_seq.NEXTVAL, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, categoryName);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Category added successfully!");
            categoryNameField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddCategoryForm().setVisible(true));
    }
}
