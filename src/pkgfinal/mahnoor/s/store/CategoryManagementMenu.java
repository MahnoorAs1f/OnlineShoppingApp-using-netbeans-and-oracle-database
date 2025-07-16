package pkgfinal.mahnoor.s.store;


import javax.swing.*;
import java.awt.*;

public class CategoryManagementMenu extends JFrame {
    private JButton addButton, removeButton, updateButton, viewButton, backButton;

    public CategoryManagementMenu() {
        setTitle("Category Management");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 245)); // Light background

        // Header
        JLabel header = new JLabel("Category Management", SwingConstants.CENTER);
        header.setFont(new Font("Sans Serif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBackground(new Color(35, 45, 65)); // Navy background
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(header, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20)); // 2x2 buttons + back button row
        buttonPanel.setBackground(new Color(240, 240, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Buttons
        addButton = createStyledButton("Add Category");
        removeButton = createStyledButton("Remove Category");
        updateButton = createStyledButton("Update Category");
        viewButton = createStyledButton("View Categories");

        // Add Buttons to Panel
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(viewButton);

        // Back Button Panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(new Color(240, 240, 245));
        backButton = createStyledButton("Back");
        backButton.setPreferredSize(new Dimension(150, 40)); // Smaller back button size
        backButton.setBackground(new Color(45, 45, 45)); // Darker color for prominence
        backButton.setForeground(Color.WHITE);
        backPanel.add(backButton);

        add(buttonPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);


        // Button Actions (Existing logic remains the same)
        addButton.addActionListener(e -> {
            new AddCategoryForm().setVisible(true);
            dispose();
        });

        removeButton.addActionListener(e -> {
            new RemoveCategoryForm().setVisible(true);
            dispose();
        });

        updateButton.addActionListener(e -> {
           new UpdateCategoryPanel().setVisible(true);
            dispose();
        });

        viewButton.addActionListener(e -> {
           new DisplayCategoryPanel().setVisible(true);

            dispose();
        });

        backButton.addActionListener(e -> {
            new AdminDashboardForm().setVisible(true);
            dispose();
        });
    }

     private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Sans Serif", Font.BOLD, 16));
        button.setForeground(new Color(45, 45, 45)); // Dark text
        button.setBackground(Color.WHITE); // White button
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Light border
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 245)); // Light purple on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE); // Back to white on exit
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CategoryManagementMenu().setVisible(true));
    }
}