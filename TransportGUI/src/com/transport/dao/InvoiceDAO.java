package com.transport.dao;

import com.transport.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO Invoice (trip_id, total_amount, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoice.getTripId());
            stmt.setDouble(2, invoice.getAmount());
            stmt.setString(3, invoice.getStatus());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invoices.add(new Invoice(
                        rs.getInt("id"),
                        rs.getInt("trip_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return invoices;
    }
}
