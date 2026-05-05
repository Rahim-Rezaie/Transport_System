package com.transport.dao;

import com.transport.model.Shipment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {
    public void addShipment(Shipment shipment) {
        String sql = "INSERT INTO Shipment (trip_id, goods_type, weight_kg) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipment.getTripId());
            stmt.setString(2, shipment.getDescription());
            stmt.setDouble(3, shipment.getWeight());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Shipment> getAllShipments() {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM Shipment";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                shipments.add(new Shipment(
                        rs.getInt("id"),
                        rs.getInt("trip_id"),
                        rs.getString("goods_type"),
                        rs.getDouble("weight_kg")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return shipments;
    }
}
