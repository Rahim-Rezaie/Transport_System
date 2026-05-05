package com.transport.ui;

import com.transport.dao.InvoiceDAO;
import com.transport.dao.TripDAO;
import com.transport.dao.TruckDAO;
import com.transport.model.Invoice;
import com.transport.model.Trip;
import com.transport.model.Truck;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Theme: Deep Navy + Amber Gold  —  Command-centre feel
 */
public class DashboardPanel extends JPanel {

    // ── Palette ──────────────────────────────────────────────────────────
    private static final Color BG          = new Color(10,  22,  40);
    private static final Color CARD_BG     = new Color(16,  32,  58);
    private static final Color HEADER_BG   = new Color(13,  27,  50);
    private static final Color AMBER       = new Color(233, 196, 106);
    private static final Color GREEN       = new Color(42,  202, 136);
    private static final Color CYAN        = new Color(76,  201, 240);
    private static final Color RED         = new Color(230, 57,  70);
    private static final Color TEXT        = new Color(220, 230, 255);
    private static final Color MUTED       = new Color(120, 140, 180);

    // ── Labels ───────────────────────────────────────────────────────────
    private JLabel activeTripsLabel;
    private JLabel availableTrucksLabel;
    private JLabel unpaidInvoicesLabel;
    private JLabel totalRevenueLabel;

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);

        // Header
        add(PanelStyle.buildHeader("📊", "Dashboard",
                "Live overview of your transport operations",
                BG, HEADER_BG, AMBER), BorderLayout.NORTH);

        // Cards grid
        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setBackground(BG);
        grid.setBorder(new EmptyBorder(20, 20, 12, 20));

        activeTripsLabel    = new JLabel("", SwingConstants.CENTER);
        availableTrucksLabel = new JLabel("", SwingConstants.CENTER);
        unpaidInvoicesLabel  = new JLabel("", SwingConstants.CENTER);
        totalRevenueLabel    = new JLabel("", SwingConstants.CENTER);

        grid.add(buildCard(activeTripsLabel,     "🗺  Active Trips",    CYAN));
        grid.add(buildCard(availableTrucksLabel, "🚛  Available Trucks", GREEN));
        grid.add(buildCard(unpaidInvoicesLabel,  "🧾  Unpaid Invoices",  RED));
        grid.add(buildCard(totalRevenueLabel,    "💰  Total Revenue",    AMBER));

        add(grid, BorderLayout.CENTER);

        // Bottom bar
        JButton refreshBtn = PanelStyle.buildButton("⟳  Refresh", AMBER, new Color(20, 20, 20));
        refreshBtn.addActionListener(e -> refreshDashboard());
        add(PanelStyle.buildButtonBar(HEADER_BG, refreshBtn), BorderLayout.SOUTH);

        refreshDashboard();
    }

    private JPanel buildCard(JLabel valueLabel, String title, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accent.darker().darker(), 1),
                        new EmptyBorder(20, 20, 20, 20))));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLbl.setForeground(MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(accent);

        card.add(titleLbl,   BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    public void refreshDashboard() {
        int activeTrips = 0;
        for (Trip t : new TripDAO().getAllTrips())
            if ("Active".equalsIgnoreCase(t.getStatus())) activeTrips++;

        int availableTrucks = 0;
        for (Truck t : new TruckDAO().getAllTrucks())
            if ("Available".equalsIgnoreCase(t.getStatus())) availableTrucks++;

        int unpaidInvoices = 0;
        double totalRevenue = 0;
        for (Invoice i : new InvoiceDAO().getAllInvoices()) {
            if ("Unpaid".equalsIgnoreCase(i.getStatus())) unpaidInvoices++;
            totalRevenue += i.getAmount();
        }

        activeTripsLabel.setText(String.valueOf(activeTrips));
        availableTrucksLabel.setText(String.valueOf(availableTrucks));
        unpaidInvoicesLabel.setText(String.valueOf(unpaidInvoices));
        totalRevenueLabel.setText("$" + String.format("%.0f", totalRevenue));
    }
}
