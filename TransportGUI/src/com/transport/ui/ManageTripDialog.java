package com.transport.ui;

import com.transport.dao.ExpenseDAO;
import com.transport.dao.ShipmentDAO;
import com.transport.dao.TripDAO;
import com.transport.model.Expense;
import com.transport.model.Shipment;
import com.transport.model.Trip;

import javax.swing.*;
import java.awt.*;

public class ManageTripDialog extends JDialog {
    private Trip trip;
    private TripPanel parentPanel;
    private TripDAO tripDAO;
    private ShipmentDAO shipmentDAO;
    private ExpenseDAO expenseDAO;

    public ManageTripDialog(Frame owner, Trip trip, TripPanel parentPanel) {
        super(owner, "Manage Trip #" + trip.getId(), true);
        this.trip = trip;
        this.parentPanel = parentPanel;
        this.tripDAO = new TripDAO();
        this.shipmentDAO = new ShipmentDAO();
        this.expenseDAO = new ExpenseDAO();

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 1, 10, 10));

        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.add(new JLabel("Trip ID: " + trip.getId() + " | Status: " + trip.getStatus() + " | Commission: $" + trip.getCommission()));

        JButton addShipmentBtn = new JButton("Add Shipment");
        addShipmentBtn.addActionListener(e -> showAddShipmentDialog());

        JButton addExpenseBtn = new JButton("Add Expense");
        addExpenseBtn.addActionListener(e -> showAddExpenseDialog());

        JButton updateCommissionBtn = new JButton("Update Commission");
        updateCommissionBtn.addActionListener(e -> showUpdateCommissionDialog());

        add(infoPanel);
        add(addShipmentBtn);
        add(addExpenseBtn);
        add(updateCommissionBtn);
    }

    private void showAddShipmentDialog() {
        JTextField descField = new JTextField(15);
        JTextField weightField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Description:")); panel.add(descField);
        panel.add(new JLabel("Weight:")); panel.add(weightField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Add Shipment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double weight = Double.parseDouble(weightField.getText());
                shipmentDAO.addShipment(new Shipment(0, trip.getId(), descField.getText(), weight));
                JOptionPane.showMessageDialog(this, "Shipment added.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid weight.");
            }
        }
    }

    private void showAddExpenseDialog() {
        JTextField typeField = new JTextField(15);
        JTextField amountField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Type (e.g. Fuel):")); panel.add(typeField);
        panel.add(new JLabel("Amount:")); panel.add(amountField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                expenseDAO.addExpense(new Expense(0, trip.getId(), typeField.getText(), amount));
                JOptionPane.showMessageDialog(this, "Expense added.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        }
    }

    private void showUpdateCommissionDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter new commission amount:", trip.getCommission());
        if (input != null) {
            try {
                double comm = Double.parseDouble(input);
                tripDAO.updateCommission(trip.getId(), comm);
                trip.setCommission(comm);
                parentPanel.refreshTable();
                JOptionPane.showMessageDialog(this, "Commission updated.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid commission.");
            }
        }
    }
}
