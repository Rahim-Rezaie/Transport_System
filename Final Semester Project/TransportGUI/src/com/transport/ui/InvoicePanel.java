package com.transport.ui;

import com.transport.dao.ExpenseDAO;
import com.transport.dao.InvoiceDAO;
import com.transport.dao.ShipmentDAO;
import com.transport.dao.TripDAO;
import com.transport.model.Expense;
import com.transport.model.Invoice;
import com.transport.model.Shipment;
import com.transport.model.Trip;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoicePanel extends JPanel {
    private JTable table;
    private InvoiceDAO invoiceDAO;
    private DefaultTableModel model;

    public InvoicePanel() {
        invoiceDAO = new InvoiceDAO();
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Trip ID", "Amount", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton generateBtn = new JButton("Generate Invoice");
        bottomPanel.add(refreshBtn);
        bottomPanel.add(generateBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshTable());
        generateBtn.addActionListener(e -> showGenerateInvoiceDialog());

        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        for (Invoice inv : invoices) {
            model.addRow(new Object[]{inv.getId(), inv.getTripId(), inv.getAmount(), inv.getStatus()});
        }
    }

    private void showGenerateInvoiceDialog() {
        TripDAO tripDAO = new TripDAO();
        ShipmentDAO shipmentDAO = new ShipmentDAO();
        ExpenseDAO expenseDAO = new ExpenseDAO();

        JComboBox<String> tripBox = new JComboBox<>();
        for (Trip t : tripDAO.getAllTrips()) {
            if ("Completed".equalsIgnoreCase(t.getStatus())) {
                tripBox.addItem(t.getId() + " - Truck " + t.getTruckId() + " / Driver " + t.getDriverId());
            }
        }

        if (tripBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No completed trips found without invoices.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Select Completed Trip:"));
        panel.add(tripBox);
        panel.add(new JLabel("Status:"));
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Unpaid", "Paid"});
        panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Generate Invoice", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int tripId = Integer.parseInt(((String) tripBox.getSelectedItem()).split(" ")[0]);
            
            // Calculate base amount from shipments (e.g., rate of $5 per unit weight)
            double totalAmount = 0.0;
            for (Shipment s : shipmentDAO.getAllShipments()) {
                if (s.getTripId() == tripId) totalAmount += s.getWeight() * 5.0; // simple mock formula
            }
            // Add expenses
            for (Expense e : expenseDAO.getAllExpenses()) {
                if (e.getTripId() == tripId) totalAmount += e.getAmount();
            }
            
            // Get commission
            double commission = 0.0;
            for (Trip t : tripDAO.getAllTrips()) {
                if (t.getId() == tripId) commission = t.getCommission();
            }
            totalAmount += commission;

            invoiceDAO.addInvoice(new Invoice(0, tripId, totalAmount, (String) statusBox.getSelectedItem()));
            refreshTable();
            JOptionPane.showMessageDialog(this, "Invoice generated for $" + totalAmount);
        }
    }
}