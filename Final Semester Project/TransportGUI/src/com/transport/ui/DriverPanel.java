package com.transport.ui;

import com.transport.dao.DriverDAO;
import com.transport.model.Driver;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverPanel extends JPanel {
    private JTable table;
    private DriverDAO driverDAO;
    private DefaultTableModel model;

    public DriverPanel() {
        driverDAO = new DriverDAO();
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Phone", "License", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton newBtn = new JButton("New Driver");
        bottomPanel.add(refreshBtn);
        bottomPanel.add(newBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshTable());
        newBtn.addActionListener(e -> showAddDriverDialog());

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver d : drivers) {
            model.addRow(new Object[]{d.getId(), d.getName(), d.getPhone(), d.getLicenseNo(), d.getStatus()});
        }
    }

    private void showAddDriverDialog() {
        JTextField nameField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField licenseField = new JTextField(15);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Active", "Inactive"});

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("License:")); panel.add(licenseField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Driver", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Driver newDriver = new Driver(0, nameField.getText(), phoneField.getText(), licenseField.getText(), (String) statusBox.getSelectedItem());
            driverDAO.addDriver(newDriver);
            refreshTable();
        }
    }
}
