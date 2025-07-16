package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;        
import javax.imageio.ImageIO;

public class SHOPAHOLICC extends JFrame {

    public SHOPAHOLICC() {
        setTitle("Shopaholicc - Your Ultimate Shopping Companion");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background Panel with Image
        JPanel backgroundPanel = new JPanel() {
            BufferedImage bgImage;

            {
                try {
                    // Load the background image
                    bgImage = ImageIO.read(new File("C:\\Users\\PMLS\\Downloads\\shopp.JPG")); // Update path to your image
                } catch (Exception e) {
                    System.out.println("Failed to load image: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    // Draw the image resized to the panel size
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

                    // Add dimming effect
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(0, 0, 0, 100)); // Transparent black overlay
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // Header Label
        JLabel headerLabel = new JLabel("Welcome to Shopaholicc!", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 50)); // Bigger font size
        headerLabel.setForeground(new Color(255, 255, 255)); // White text
        headerLabel.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));
        backgroundPanel.add(headerLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 20, 20)); // Spacing between buttons
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 400, 100, 400)); // Padding for better alignment
        buttonPanel.setOpaque(false);

        JButton adminButton = createModernButton("Admin");
        JButton customerButton = createModernButton("Customer");

        buttonPanel.add(adminButton);
        buttonPanel.add(customerButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // Customer Button Action
        customerButton.addActionListener(e -> {
            CustomerRegistrationForm registrationForm = new CustomerRegistrationForm();
            registrationForm.setVisible(true);
            dispose(); // Close Main Menu
        });

        // Admin Button Action
        adminButton.addActionListener(e -> {
            AdminLoginForm adminLogin = new AdminLoginForm();
            adminLogin.setVisible(true);
            dispose(); // Close Main Menu
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(250, 60)); // Button size

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
        SwingUtilities.invokeLater(() -> {
            SHOPAHOLICC menu = new SHOPAHOLICC();
            menu.setVisible(true);
        });
    }
}
