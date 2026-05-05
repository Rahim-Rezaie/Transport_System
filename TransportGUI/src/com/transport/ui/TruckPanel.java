package com.transport.ui;

import com.transport.dao.TruckDAO;
import com.transport.model.Truck;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Theme: Charcoal Steel + Burnt Orange  —  Industrial / Mechanical feel
 */
public class TruckPanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG        = new Color(15, 15, 15);
    private static final Color CARD_BG   = new Color(26, 26, 26);
    private static final Color ROW_ALT   = new Color(33, 33, 33);
    private static final Color HEADER_BG = new Color(20, 20, 20);
    private static final Color ORANGE    = new Color(247, 127,  0);
    private static final Color TEXT      = new Color(235, 225, 210);

    private JTable            table;
    private TruckDAO          truckDAO;
    private DefaultTableModel model;

    public TruckPanel() {
        truckDAO = new TruckDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("🚛", "Trucks",
                "Fleet management — track every vehicle and its availability",
                BG, HEADER_BG, ORANGE), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Truck Number", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = PanelStyle.buildStyledTable(model, CARD_BG, ROW_ALT, HEADER_BG, ORANGE, TEXT);

        JScrollPane sp = new JScrollPane(table);
        PanelStyle.styleScrollPane(sp, CARD_BG, ORANGE.darker().darker());
        add(sp, BorderLayout.CENTER);

        // Buttons
        JButton refreshBtn = PanelStyle.buildButton("⟳  Refresh",   ORANGE, new Color(15, 10, 5));
        JButton newBtn     = PanelStyle.buildButton("＋  New Truck", new Color(60, 35, 5), TEXT);
        refreshBtn.addActionListener(e -> refreshTable());
        newBtn.addActionListener(e -> showAddTruckDialog());
        add(PanelStyle.buildButtonBar(HEADER_BG, refreshBtn, newBtn), BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Truck> trucks = truckDAO.getAllTrucks();
        for (Truck t : trucks)
            model.addRow(new Object[]{t.getId(), t.getNumber(), t.getStatus()});
    }

    private void showAddTruckDialog() {
        JTextField numberField = new JTextField(15);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Available", "On Trip", "Maintenance"});

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBorder(new javax.swing.border.EmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Truck Number:")); panel.add(numberField);
        panel.add(new JLabel("Status:"));       panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Truck",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            truckDAO.addTruck(new Truck(0, numberField.getText(), (String) statusBox.getSelectedItem()));
            refreshTable();
        }
    }
}
