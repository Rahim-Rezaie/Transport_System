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

/**
 * Theme: Dark Gold + Amber Yellow  —  Finance / Money feel
 */
public class InvoicePanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG        = new Color(20, 16,  0);
    private static final Color CARD_BG   = new Color(34, 28,  0);
    private static final Color ROW_ALT   = new Color(44, 36,  0);
    private static final Color HEADER_BG = new Color(26, 20,  0);
    private static final Color GOLD      = new Color(255, 214, 10);
    private static final Color TEXT      = new Color(255, 248, 200);

    private JTable            table;
    private InvoiceDAO        invoiceDAO;
    private DefaultTableModel model;

    public InvoicePanel() {
        invoiceDAO = new InvoiceDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("🧾", "Invoices",
                "Generate and track invoices — monitor paid and unpaid bills",
                BG, HEADER_BG, GOLD), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Trip ID", "Amount ($)", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = PanelStyle.buildStyledTable(model, CARD_BG, ROW_ALT, HEADER_BG, GOLD, TEXT);

        JScrollPane sp = new JScrollPane(table);
        PanelStyle.styleScrollPane(sp, CARD_BG, GOLD.darker().darker());
        add(sp, BorderLayout.CENTER);

        // Buttons
        JButton refreshBtn  = PanelStyle.buildButton("⟳  Refresh",          GOLD, new Color(18, 14, 0));
        JButton generateBtn = PanelStyle.buildButton("＋  Generate Invoice", new Color(80, 65, 0), TEXT);
        refreshBtn.addActionListener(e -> refreshTable());
        generateBtn.addActionListener(e -> showGenerateInvoiceDialog());
        add(PanelStyle.buildButtonBar(HEADER_BG, refreshBtn, generateBtn), BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        for (Invoice inv : invoices)
            model.addRow(new Object[]{inv.getId(), inv.getTripId(),
                    String.format("$%.2f", inv.getAmount()), inv.getStatus()});
    }

    private void showGenerateInvoiceDialog() {
        TripDAO     tripDAO     = new TripDAO();
        ShipmentDAO shipmentDAO = new ShipmentDAO();
        ExpenseDAO  expenseDAO  = new ExpenseDAO();

        JComboBox<String> tripBox = new JComboBox<>();
        for (Trip t : tripDAO.getAllTrips())
            if ("Completed".equalsIgnoreCase(t.getStatus()))
                tripBox.addItem(t.getId() + " - Truck " + t.getTruckId() + " / Driver " + t.getDriverId());

        if (tripBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No completed trips found.");
            return;
        }

        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Unpaid", "Paid"});
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(new javax.swing.border.EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Completed Trip:")); panel.add(tripBox);
        panel.add(new JLabel("Status:"));         panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Generate Invoice",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            int tripId = Integer.parseInt(((String) tripBox.getSelectedItem()).split(" ")[0]);

            double total = 0;
            for (Shipment s : shipmentDAO.getAllShipments())
                if (s.getTripId() == tripId) total += s.getWeight() * 5.0;
            for (Expense e : expenseDAO.getAllExpenses())
                if (e.getTripId() == tripId) total += e.getAmount();
            for (Trip t : tripDAO.getAllTrips())
                if (t.getId() == tripId) total += t.getCommission();

            invoiceDAO.addInvoice(new Invoice(0, tripId, total, (String) statusBox.getSelectedItem()));
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    String.format("Invoice generated: $%.2f", total), "Done",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}