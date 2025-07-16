package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRegistrationForm extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField;
    private JButton registerButton, loginButton;

    public CustomerRegistrationForm() {
        setTitle("Customer Registration");
        setSize(600, 500); // Adjusted for modern look
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the frame on screen

        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\PMLS\\Downloads\\OIP.JPEG"); // Path to your background image
                Image backgroundImage = backgroundIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Header
        JLabel headerLabel = new JLabel("Customer Registration", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(255, 255, 255)); // Light color for readability
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15)); // Grid for labels and fields
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 18);

        // Input fields
        firstNameField = createModernTextField(fieldFont);
        lastNameField = createModernTextField(fieldFont);
        emailField = createModernTextField(fieldFont);
        phoneField = createModernTextField(fieldFont);
        addressField = createModernTextField(fieldFont);

        formPanel.add(createModernLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(createModernLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(createModernLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(createModernLabel("Phone Number:"));
        formPanel.add(phoneField);
        formPanel.add(createModernLabel("Address:"));
        formPanel.add(addressField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 20, 50));

        registerButton = createModernButton("Register");
        loginButton = createModernButton("Login");

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add to frame
        add(mainPanel);

        // Button Actions
        registerButton.addActionListener(e -> registerCustomer());
        loginButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new CustomerLoginForm().setVisible(true));
            dispose();
        });
    }

    private JTextField createModernTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 250), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        textField.setPreferredSize(new Dimension(200, 30)); // Make the text fields wider
        return textField;
    }

    private JLabel createModernLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(255, 255, 255)); // Light color for readability
        return label;
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(new Color(100, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 120, 230));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 250));
            }
        });

        return button;
    }

    private void registerCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        if (!validateInput()) {
            return; // Exit if validation fails
        }

        String getNextIdSql = "SELECT NVL(MAX(CustomerID), 0) + 1 AS NextID FROM Customer";
        String insertSql = "INSERT INTO Customer (CustomerID, FirstName, LastName, Email, Phone, Address) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getIdStmt = conn.prepareStatement(getNextIdSql);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            ResultSet rs = getIdStmt.executeQuery();
            int customerId = 0;
            if (rs.next()) {
                customerId = rs.getInt("NextID");
            }

            pstmt.setInt(1, customerId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, address);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                new DisplayCategories(customerId).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                addressField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerRegistrationForm().setVisible(true));
    }
}
