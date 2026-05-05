package com.transport.ui;

import com.transport.dao.InvoiceDAO;
import com.transport.dao.TripDAO;
import com.transport.dao.TruckDAO;
import com.transport.model.Invoice;
import com.transport.model.Trip;
import com.transport.model.Truck;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel activeTripsLabel;
    private JLabel availableTrucksLabel;
    private JLabel unpaidInvoicesLabel;
    private JLabel totalRevenueLabel;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        activeTripsLabel = createMetricLabel("Active Trips", "0");
        availableTrucksLabel = createMetricLabel("Available Trucks", "0");
        unpaidInvoicesLabel = createMetricLabel("Unpaid Invoices", "0");
        totalRevenueLabel = createMetricLabel("Total Revenue", "$0.00");

        gridPanel.add(createCard(activeTripsLabel));
        gridPanel.add(createCard(availableTrucksLabel));
        gridPanel.add(createCard(unpaidInvoicesLabel));
        gridPanel.add(createCard(totalRevenueLabel));

        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Dashboard");
        refreshBtn.addActionListener(e -> refreshDashboard());
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshDashboard();
    }

    private JLabel createMetricLabel(String title, String value) {
        return new JLabel("<html><center><h2>" + title + "</h2><h1>" + value + "</h1></center></html>", SwingConstants.CENTER);
    }

    private JPanel createCard(JLabel label) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    public void refreshDashboard() {
        int activeTrips = 0;
        for (Trip t : new TripDAO().getAllTrips()) {
            if ("Active".equalsIgnoreCase(t.getStatus())) activeTrips++;
        }
        
        int availableTrucks = 0;
        for (Truck t : new TruckDAO().getAllTrucks()) {
            if ("Available".equalsIgnoreCase(t.getStatus())) availableTrucks++;
        }

        int unpaidInvoices = 0;
        double totalRevenue = 0;
        for (Invoice i : new InvoiceDAO().getAllInvoices()) {
            if ("Unpaid".equalsIgnoreCase(i.getStatus())) unpaidInvoices++;
            totalRevenue += i.getAmount();
        }

        activeTripsLabel.setText("<html><center><h2>Active Trips</h2><h1>" + activeTrips + "</h1></center></html>");
        availableTrucksLabel.setText("<html><center><h2>Available Trucks</h2><h1>" + availableTrucks + "</h1></center></html>");
        unpaidInvoicesLabel.setText("<html><center><h2>Unpaid Invoices</h2><h1>" + unpaidInvoices + "</h1></center></html>");
        totalRevenueLabel.setText("<html><center><h2>Total Revenue</h2><h1>$" + String.format("%.2f", totalRevenue) + "</h1></center></html>");
    }
}
