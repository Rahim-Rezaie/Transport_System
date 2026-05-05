package com.transport.ui;

import com.transport.dao.DriverDAO;
import com.transport.dao.TripDAO;
import com.transport.dao.TruckDAO;
import com.transport.model.Driver;
import com.transport.model.Trip;
import com.transport.model.Truck;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TripPanel extends JPanel {
    private JTable table;
    private TripDAO tripDAO;
    private DefaultTableModel model;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public TripPanel() {
        tripDAO = new TripDAO();
        setLayout(new BorderLayout());

        String[] columns = { "ID", "Truck ID", "Driver ID", "Start Date", "End Date", "Status", "Commission" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int tripId = (int) table.getValueAt(row, 0);
                        Trip selectedTrip = null;
                        for (Trip t : tripDAO.getAllTrips()) {
                            if (t.getId() == tripId) selectedTrip = t;
                        }
                        if (selectedTrip != null) {
                            Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(TripPanel.this);
                            new ManageTripDialog(topFrame, selectedTrip, TripPanel.this).setVisible(true);
                        }
                    }
                }
            }
        });

        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton createBtn = new JButton("Create Trip");
        bottomPanel.add(refreshBtn);
        bottomPanel.add(createBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshTable());
        createBtn.addActionListener(e -> showCreateTripDialog());

        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Trip> trips = tripDAO.getAllTrips();
        for (Trip t : trips) {
            model.addRow(new Object[]{
                t.getId(), t.getTruckId(), t.getDriverId(), 
                t.getStartDate() != null ? sdf.format(t.getStartDate()) : "", 
                t.getEndDate() != null ? sdf.format(t.getEndDate()) : "", 
                t.getStatus(), t.getCommission()
            });
        }
    }

    private void showCreateTripDialog() {
        JComboBox<String> truckBox = new JComboBox<>();
        List<Truck> trucks = new TruckDAO().getAllTrucks();
        for (Truck t : trucks) truckBox.addItem(t.getId() + " - " + t.getNumber());

        JComboBox<String> driverBox = new JComboBox<>();
        List<Driver> drivers = new DriverDAO().getAllDrivers();
        for (Driver d : drivers) driverBox.addItem(d.getId() + " - " + d.getName());

        JTextField startDateField = new JTextField(10);
        startDateField.setToolTipText("yyyy-MM-dd");
        JTextField endDateField = new JTextField(10);
        endDateField.setToolTipText("yyyy-MM-dd");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Planned", "Active", "Completed"});

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Truck:")); panel.add(truckBox);
        panel.add(new JLabel("Driver:")); panel.add(driverBox);
        panel.add(new JLabel("Start Date (yyyy-MM-dd):")); panel.add(startDateField);
        panel.add(new JLabel("End Date (yyyy-MM-dd):")); panel.add(endDateField);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Trip", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int truckId = Integer.parseInt(((String) truckBox.getSelectedItem()).split(" ")[0]);
                int driverId = Integer.parseInt(((String) driverBox.getSelectedItem()).split(" ")[0]);
                java.util.Date startDate = startDateField.getText().isEmpty() ? new java.util.Date() : sdf.parse(startDateField.getText());
                java.util.Date endDate = endDateField.getText().isEmpty() ? new java.util.Date() : sdf.parse(endDateField.getText());

                Trip newTrip = new Trip(0, truckId, driverId, startDate, endDate, (String) statusBox.getSelectedItem(), 0.0);
                tripDAO.addTrip(newTrip);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error parsing input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
