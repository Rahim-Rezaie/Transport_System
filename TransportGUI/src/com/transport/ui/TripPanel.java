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

/**
 * Theme: Deep Teal + Electric Cyan  —  Road / Journey feel
 */
public class TripPanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG        = new Color( 4, 22, 30);
    private static final Color CARD_BG   = new Color( 8, 36, 50);
    private static final Color ROW_ALT   = new Color(11, 44, 62);
    private static final Color HEADER_BG = new Color( 6, 28, 40);
    private static final Color CYAN      = new Color( 0, 180, 216);
    private static final Color TEXT      = new Color(200, 235, 245);

    private JTable            table;
    private TripDAO           tripDAO;
    private DefaultTableModel model;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public TripPanel() {
        tripDAO = new TripDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("🗺", "Trips",
                "Create and manage trips — assign drivers and trucks, track progress",
                BG, HEADER_BG, CYAN), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Truck ID", "Driver ID", "Start Date", "End Date", "Status", "Commission"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = PanelStyle.buildStyledTable(model, CARD_BG, ROW_ALT, HEADER_BG, CYAN, TEXT);

        // Double-click to manage
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int tripId = (int) table.getValueAt(row, 0);
                        for (Trip t : tripDAO.getAllTrips()) {
                            if (t.getId() == tripId) {
                                Frame f = (Frame) SwingUtilities.getWindowAncestor(TripPanel.this);
                                new ManageTripDialog(f, t, TripPanel.this).setVisible(true);
                                break;
                            }
                        }
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(table);
        PanelStyle.styleScrollPane(sp, CARD_BG, CYAN.darker().darker());
        add(sp, BorderLayout.CENTER);

        // Buttons
        JButton refreshBtn = PanelStyle.buildButton("⟳  Refresh",     CYAN, new Color(5, 15, 20));
        JButton createBtn  = PanelStyle.buildButton("＋  Create Trip", new Color(0, 60, 80), TEXT);
        refreshBtn.addActionListener(e -> refreshTable());
        createBtn.addActionListener(e -> showCreateTripDialog());
        add(PanelStyle.buildButtonBar(HEADER_BG, refreshBtn, createBtn), BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        for (Trip t : tripDAO.getAllTrips())
            model.addRow(new Object[]{
                t.getId(), t.getTruckId(), t.getDriverId(),
                t.getStartDate() != null ? sdf.format(t.getStartDate()) : "",
                t.getEndDate()   != null ? sdf.format(t.getEndDate())   : "",
                t.getStatus(), t.getCommission()
            });
    }

    private void showCreateTripDialog() {
        JComboBox<String> truckBox  = new JComboBox<>();
        JComboBox<String> driverBox = new JComboBox<>();

        for (Truck t  : new TruckDAO().getAllTrucks())   truckBox.addItem(t.getId()  + " - " + t.getNumber());
        for (Driver d : new DriverDAO().getAllDrivers()) driverBox.addItem(d.getId() + " - " + d.getName());

        JTextField startField = new JTextField(10);
        startField.setToolTipText("yyyy-MM-dd");
        JTextField endField = new JTextField(10);
        endField.setToolTipText("yyyy-MM-dd");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Planned", "Active", "Completed"});

        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(new javax.swing.border.EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Truck:"));                   panel.add(truckBox);
        panel.add(new JLabel("Driver:"));                  panel.add(driverBox);
        panel.add(new JLabel("Start Date (yyyy-MM-dd):")); panel.add(startField);
        panel.add(new JLabel("End Date (yyyy-MM-dd):"));   panel.add(endField);
        panel.add(new JLabel("Status:"));                  panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Trip",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int truckId  = Integer.parseInt(((String) truckBox.getSelectedItem()).split(" ")[0]);
                int driverId = Integer.parseInt(((String) driverBox.getSelectedItem()).split(" ")[0]);
                java.util.Date start = startField.getText().isEmpty() ? new java.util.Date() : sdf.parse(startField.getText());
                java.util.Date end   = endField.getText().isEmpty()   ? new java.util.Date() : sdf.parse(endField.getText());
                tripDAO.addTrip(new Trip(0, truckId, driverId, start, end, (String) statusBox.getSelectedItem(), 0.0));
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
