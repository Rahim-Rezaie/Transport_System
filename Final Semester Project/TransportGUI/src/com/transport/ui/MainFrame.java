package com.transport.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Transport Management System");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centre on screen

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // 🔍 Search is the first tab — most prominent
        tabs.addTab("🔍 Search",    new SearchPanel());
        tabs.addTab("📊 Dashboard", new DashboardPanel());
        tabs.addTab("👤 Drivers",   new DriverPanel());
        tabs.addTab("🚛 Trucks",    new TruckPanel());
        tabs.addTab("🗺 Trips",     new TripPanel());
        tabs.addTab("📦 Shipments", new ShipmentPanel());
        tabs.addTab("💸 Expenses",  new ExpensePanel());
        tabs.addTab("🧾 Invoices",  new InvoicePanel());

        add(tabs);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
