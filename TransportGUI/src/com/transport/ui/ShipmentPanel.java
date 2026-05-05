package com.transport.ui;

import com.transport.dao.ShipmentDAO;
import com.transport.model.Shipment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Theme: Ocean Navy + Sky Blue  —  Cargo / Sea freight feel
 */
public class ShipmentPanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG        = new Color( 4, 14, 34);
    private static final Color CARD_BG   = new Color( 8, 24, 54);
    private static final Color ROW_ALT   = new Color(10, 30, 66);
    private static final Color HEADER_BG = new Color( 6, 18, 44);
    private static final Color SKY       = new Color(76, 201, 240);
    private static final Color TEXT      = new Color(200, 230, 255);

    private JTable            table;
    private ShipmentDAO       shipmentDAO;
    private DefaultTableModel model;

    public ShipmentPanel() {
        shipmentDAO = new ShipmentDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("📦", "Shipments",
                "All cargo shipments linked to their trips",
                BG, HEADER_BG, SKY), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Trip ID", "Description", "Weight (kg)"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = PanelStyle.buildStyledTable(model, CARD_BG, ROW_ALT, HEADER_BG, SKY, TEXT);

        JScrollPane sp = new JScrollPane(table);
        PanelStyle.styleScrollPane(sp, CARD_BG, SKY.darker().darker());
        add(sp, BorderLayout.CENTER);

        // Buttons
        JButton addBtn = PanelStyle.buildButton("+  Add Shipment", SKY, new Color(4, 12, 28));
        JButton refreshBtn = PanelStyle.buildButton("⟳  Refresh", SKY, new Color(4, 12, 28));
        
        addBtn.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow instanceof Frame) {
                AddShipmentDialog dialog = new AddShipmentDialog((Frame) parentWindow);
                dialog.setVisible(true);
                if (dialog.isSuccess()) {
                    refreshTable();
                }
            }
        });
        refreshBtn.addActionListener(e -> refreshTable());
        
        add(PanelStyle.buildButtonBar(HEADER_BG, addBtn, refreshBtn), BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Shipment> shipments = shipmentDAO.getAllShipments();
        for (Shipment s : shipments)
            model.addRow(new Object[]{s.getId(), s.getTripId(), s.getDescription(), s.getWeight()});
    }
}
