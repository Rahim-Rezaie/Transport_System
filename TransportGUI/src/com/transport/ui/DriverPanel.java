package com.transport.ui;

import com.transport.dao.DriverDAO;
import com.transport.model.Driver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Theme: Deep Indigo + Electric Violet  —  Identity / People feel
 */
public class DriverPanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG        = new Color(14,  8,  32);
    private static final Color CARD_BG   = new Color(22, 14, 50);
    private static final Color ROW_ALT   = new Color(28, 18, 60);
    private static final Color HEADER_BG = new Color(18, 10, 40);
    private static final Color VIOLET    = new Color(199, 125, 255);
    private static final Color TEXT      = new Color(225, 215, 255);

    private JTable             table;
    private DriverDAO          driverDAO;
    private DefaultTableModel  model;

    public DriverPanel() {
        driverDAO = new DriverDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("👤", "Drivers",
                "Manage all drivers — licenses, status and assignments",
                BG, HEADER_BG, VIOLET), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Phone", "License No.", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = PanelStyle.buildStyledTable(model, CARD_BG, ROW_ALT, HEADER_BG, VIOLET, TEXT);

        JScrollPane sp = new JScrollPane(table);
        PanelStyle.styleScrollPane(sp, CARD_BG, VIOLET.darker().darker());
        add(sp, BorderLayout.CENTER);

        // Buttons
        JButton refreshBtn = PanelStyle.buildButton("⟳  Refresh",    VIOLET, new Color(10, 5, 20));
        JButton newBtn     = PanelStyle.buildButton("＋  New Driver", new Color(60, 30, 100), TEXT);
        refreshBtn.addActionListener(e -> refreshTable());
        newBtn.addActionListener(e -> showAddDriverDialog());
        add(PanelStyle.buildButtonBar(HEADER_BG, refreshBtn, newBtn), BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver d : drivers)
            model.addRow(new Object[]{d.getId(), d.getName(), d.getPhone(), d.getLicenseNo(), d.getStatus()});
    }

    private void showAddDriverDialog() {
        JTextField nameField    = new JTextField(15);
        JTextField phoneField   = new JTextField(15);
        JTextField licenseField = new JTextField(15);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Active", "Inactive"});

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(new javax.swing.border.EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Name:"));    panel.add(nameField);
        panel.add(new JLabel("Phone:"));   panel.add(phoneField);
        panel.add(new JLabel("License:")); panel.add(licenseField);
        panel.add(new JLabel("Status:"));  panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Driver",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            driverDAO.addDriver(new Driver(0, nameField.getText(), phoneField.getText(),
                    licenseField.getText(), (String) statusBox.getSelectedItem()));
            refreshTable();
        }
    }
}
