package pkgfinal.mahnoor.s.store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DisplayCategoryPanel extends JFrame {
    private JTable categoryTable;
    private JButton goBackButton;

    public DisplayCategoryPanel() {
        setTitle("Categories");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Apply consistent LookAndFeel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Header
        JLabel headerLabel = new JLabel("Categories", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Sans Serif", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(45, 85, 155));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        // Table and ScrollPane
        categoryTable = new JTable(new DefaultTableModel(new Object[]{"Category ID", "Category Name"}, 0));
        categoryTable.setRowHeight(25);
        categoryTable.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        categoryTable.getTableHeader().setFont(new Font("Sans Serif", Font.BOLD, 16));
        categoryTable.getTableHeader().setBackground(new Color(45, 85, 155));
        categoryTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(categoryTable);
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
            new CategoryManagementMenu().setVisible(true); // Redirect to previous page
            dispose();
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        footerPanel.add(goBackButton);
        add(footerPanel, BorderLayout.SOUTH);

        // Load categories into the table
        loadCategories();
    }

    private void loadCategories() {
        String query = "SELECT CategoryID, CategoryName FROM Category";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) categoryTable.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("CategoryID"), rs.getString("CategoryName")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DisplayCategoryPanel().setVisible(true));
    }
}
