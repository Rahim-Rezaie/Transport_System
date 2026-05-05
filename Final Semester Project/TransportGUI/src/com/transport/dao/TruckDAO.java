package com.transport.dao;

import com.transport.model.Truck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TruckDAO {

    // Insert new truck
    public void addTruck(Truck truck) {
        String sql = "INSERT INTO Truck (number_plate, status) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, truck.getNumber());
            stmt.setString(2, truck.getStatus());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fetch all trucks
    public List<Truck> getAllTrucks() {
        List<Truck> trucks = new ArrayList<>();
        String sql = "SELECT * FROM Truck";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Truck t = new Truck(
                    rs.getInt("id"),
                    rs.getString("number_plate"),
                    rs.getString("status")
                );
                trucks.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return trucks;
    }
}
