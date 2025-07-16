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

import javax.swing.*;
import java.awt.*;

public class AdminDashboardForm extends JFrame {
    private JButton categoryManagementButton, productManagementButton, logoutButton;

    public AdminDashboardForm() {
        setTitle("Admin Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Apply Nimbus look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // Header Panel
        JLabel headerLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Sans Serif", Font.BOLD, 26));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(45, 85, 155));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Center Panel with Buttons
        JPanel centerPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\PMLS\\Downloads\\cart.JPEG"); // Replace with your image path
                Image backgroundImage = backgroundIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        add(centerPanel, BorderLayout.CENTER);

        // Buttons Configuration
        categoryManagementButton = createModernButton("Category Management");
        productManagementButton = createModernButton("Product Management");
        logoutButton = createModernButton("Logout");

        categoryManagementButton.setBounds(500, 200, 350, 60); // Adjust position and size
        productManagementButton.setBounds(500, 300, 350, 60); // Adjust position and size
        logoutButton.setBounds(500, 400, 350, 60); // Adjust position and size

        centerPanel.add(categoryManagementButton);
        centerPanel.add(productManagementButton);
        centerPanel.add(logoutButton);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(footerPanel, BorderLayout.SOUTH);

        // Button Actions
        categoryManagementButton.addActionListener(e -> {
           new CategoryManagementMenu().setVisible(true);
            dispose();
        });

        productManagementButton.addActionListener(e -> {
            new ProductManagementMenu().setVisible(true);
            dispose();
        });

        logoutButton.addActionListener(e -> {
            new SHOPAHOLICC().setVisible(true);
            dispose();
        });
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient for button background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(72, 61, 139), getWidth(), getHeight(), new Color(123, 104, 238));
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25); // Rounded corners

                super.paintComponent(g);
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Slightly smaller font

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(230, 230, 230)); // Slightly lighter text
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE); // Reset to white text
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardForm().setVisible(true));
    }}