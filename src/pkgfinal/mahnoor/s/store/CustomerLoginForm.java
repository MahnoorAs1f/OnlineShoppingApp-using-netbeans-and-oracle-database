package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerLoginForm extends JFrame {
    private JTextField emailField, firstNameField;
    private JButton loginButton, cancelButton;

    public CustomerLoginForm() {
        setTitle("Customer Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center on screen

        // Main Panel with Background Image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\PMLS\\Downloads\\OIP.JPEG"); // Path to your background image
                Image backgroundImage = backgroundIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Customer Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 255, 255)); // Light color for readability
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Input Fields
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        emailLabel.setForeground(new Color(255, 255, 255)); // Light color for readability
        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        firstNameLabel.setForeground(new Color(255, 255, 255)); // Light color for readability
        firstNameField = new JTextField();
        firstNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Buttons
        loginButton = createModernButton("Login", new Color(72, 201, 176)); // Green
        cancelButton = createModernButton("Cancel", new Color(245, 66, 66)); // Red

        loginButton.addActionListener(e -> loginCustomer());
        cancelButton.addActionListener(e -> dispose());

        // Adding components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(firstNameLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(firstNameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        gbc.gridy++;
        mainPanel.add(cancelButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createModernButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void loginCustomer() {
        String email = emailField.getText();
        String firstName = firstNameField.getText();

        if (email.isEmpty() || firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        String loginSql = "SELECT CustomerID FROM Customer WHERE Email = ? AND FirstName = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(loginSql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome back.");
                new DisplayCategories(customerId).setVisible(true); // Redirect to categories
                dispose(); // Close login form
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerLoginForm().setVisible(true);
        });
    }
}
