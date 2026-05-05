package com.transport.ui;

import com.transport.dao.ExpenseDAO;
import com.transport.model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Theme: Dark Crimson + Rose Red  —  Cost / Warning feel
 */
public class ExpensePanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG        = new Color(26,  5,  5);
    private static final Color CARD_BG   = new Color(40,  8,  8);
    private static final Color ROW_ALT   = new Color(50, 12, 12);
    private static final Color HEADER_BG = new Color(32,  6,  6);
    private static final Color CRIMSON   = new Color(230, 57, 70);
    private static final Color TEXT      = new Color(255, 220, 220);

    private JTable            table;
    private ExpenseDAO        expenseDAO;
    private DefaultTableModel model;

    public ExpensePanel() {
        expenseDAO = new ExpenseDAO();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("💸", "Expenses",
                "Track every cost per trip — fuel, tolls, maintenance and more",
                BG, HEADER_BG, CRIMSON), BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Trip ID", "Type", "Amount ($)"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = PanelStyle.buildStyledTable(model, CARD_BG, ROW_ALT, HEADER_BG, CRIMSON, TEXT);

        JScrollPane sp = new JScrollPane(table);
        PanelStyle.styleScrollPane(sp, CARD_BG, CRIMSON.darker().darker());
        add(sp, BorderLayout.CENTER);

        // Button
        JButton refreshBtn = PanelStyle.buildButton("⟳  Refresh", CRIMSON, new Color(20, 4, 4));
        refreshBtn.addActionListener(e -> refreshTable());
        add(PanelStyle.buildButtonBar(HEADER_BG, refreshBtn), BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Expense> expenses = expenseDAO.getAllExpenses();
        for (Expense e : expenses)
            model.addRow(new Object[]{e.getId(), e.getTripId(), e.getType(),
                    String.format("$%.2f", e.getAmount())});
    }
}
