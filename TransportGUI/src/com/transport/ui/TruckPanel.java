package com.transport.ui;

import com.transport.dao.TruckDAO;
import com.transport.model.Truck;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TruckPanel extends JPanel {
    private JTable table;
    private TruckDAO truckDAO;
    private DefaultTableModel model;

    public TruckPanel() {
        truckDAO = new TruckDAO();
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Number", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton newBtn = new JButton("New Truck");
        bottomPanel.add(refreshBtn);
        bottomPanel.add(newBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshTable());
        newBtn.addActionListener(e -> showAddTruckDialog());

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Truck> trucks = truckDAO.getAllTrucks();
        for (Truck t : trucks) {
            model.addRow(new Object[]{t.getId(), t.getNumber(), t.getStatus()});
        }
    }

    private void showAddTruckDialog() {
        JTextField numberField = new JTextField(15);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Available", "On Trip", "Maintenance"});

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Truck Number:")); panel.add(numberField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Truck", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Truck newTruck = new Truck(0, numberField.getText(), (String) statusBox.getSelectedItem());
            truckDAO.addTruck(newTruck);
            refreshTable();
        }
    }
}
