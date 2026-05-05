package com.transport.ui;

import com.transport.dao.ShipmentDAO;
import com.transport.dao.TripDAO;
import com.transport.model.Shipment;
import com.transport.model.Trip;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class AddShipmentDialog extends JDialog {
    private JComboBox<TripItem> tripCombo;
    private JTextField descField;
    private JTextField weightField;
    private ShipmentDAO shipmentDAO;
    private TripDAO tripDAO;
    private boolean success = false;

    private static final Color BG        = new Color( 4, 14, 34);
    private static final Color CARD_BG   = new Color( 8, 24, 54);
    private static final Color SKY       = new Color(76, 201, 240);
    private static final Color TEXT      = new Color(200, 230, 255);

    public AddShipmentDialog(Frame owner) {
        super(owner, "Add New Shipment", true);
        shipmentDAO = new ShipmentDAO();
        tripDAO = new TripDAO();

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("📦", "New Shipment", "Link cargo to an active trip", BG, CARD_BG, SKY), BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 20));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel lblTrip = new JLabel("Select Trip:");
        lblTrip.setForeground(TEXT);
        tripCombo = new JComboBox<>();
        loadTrips();

        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setForeground(TEXT);
        descField = new JTextField();
        styleField(descField);

        JLabel lblWeight = new JLabel("Weight (kg):");
        lblWeight.setForeground(TEXT);
        weightField = new JTextField();
        styleField(weightField);

        form.add(lblTrip); form.add(tripCombo);
        form.add(lblDesc); form.add(descField);
        form.add(lblWeight); form.add(weightField);

        add(form, BorderLayout.CENTER);

        // Buttons
        JButton saveBtn = PanelStyle.buildButton("Save Shipment", SKY, BG);
        JButton cancelBtn = PanelStyle.buildButton("Cancel", Color.GRAY, Color.WHITE);

        saveBtn.addActionListener(e -> handleSave());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnBar = PanelStyle.buildButtonBar(CARD_BG, cancelBtn, saveBtn);
        add(btnBar, BorderLayout.SOUTH);
    }

    private void styleField(JTextField field) {
        field.setBackground(CARD_BG.brighter());
        field.setForeground(Color.WHITE);
        field.setCaretColor(SKY);
        field.setBorder(BorderFactory.createLineBorder(SKY.darker()));
    }

    private void loadTrips() {
        List<Trip> trips = tripDAO.getAllTrips();
        for (Trip t : trips) {
            tripCombo.addItem(new TripItem(t));
        }
    }

    private void handleSave() {
        TripItem selected = (TripItem) tripCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a trip.");
            return;
        }

        String desc = descField.getText().trim();
        String weightStr = weightField.getText().trim();

        if (desc.isEmpty() || weightStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            Shipment s = new Shipment(selected.trip.getId(), desc, weight);
            shipmentDAO.addShipment(s);
            success = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid weight value.");
        }
    }

    public boolean isSuccess() {
        return success;
    }

    private static class TripItem {
        Trip trip;
        TripItem(Trip t) { this.trip = t; }
        @Override
        public String toString() {
            return "Trip #" + trip.getId() + " (" + trip.getStatus() + ")";
        }
    }
}
