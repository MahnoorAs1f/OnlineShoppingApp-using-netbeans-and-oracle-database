/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayProductPanel extends JFrame {
    private JTable productTable;
    private JButton goBackButton;

    public DisplayProductPanel() {
        setTitle("Products");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Apply consistent LookAndFeel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Header
        JLabel headerLabel = new JLabel("Products", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(45, 85, 155)); // Navy header
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Table and ScrollPane
        productTable = new JTable(new DefaultTableModel(new Object[]{"Product ID", "Name", "Price", "Stock", "Category"}, 0));
        productTable.setRowHeight(25);
        productTable.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        productTable.getTableHeader().setFont(new Font("Sans Serif", Font.BOLD, 16));
        productTable.getTableHeader().setBackground(new Color(45, 85, 155));
        productTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Footer with Go Back Button
        goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Sans Serif", Font.BOLD, 16));
        goBackButton.setBackground(new Color(45, 85, 155));
        goBackButton.setForeground(Color.WHITE);
        goBackButton.setFocusPainted(false);
        goBackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goBackButton.addActionListener(e -> {
            new ProductManagementMenu().setVisible(true); // Redirect to Product Management Menu
            dispose();
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        footerPanel.add(goBackButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Load products into the table
        loadProducts();
    }

    private void loadProducts() {
        String query = "SELECT p.ProductID, p.Name, p.Price, p.Stock, c.CategoryName " +
                       "FROM Product p " +
                       "JOIN Category c ON p.CategoryID = c.CategoryID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock"),
                        rs.getString("CategoryName")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DisplayProductPanel().setVisible(true));
    }
}
