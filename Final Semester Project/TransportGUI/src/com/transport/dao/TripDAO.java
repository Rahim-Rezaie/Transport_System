package com.transport.dao;

import com.transport.model.Trip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    // Insert new trip
    public void addTrip(Trip trip) {
        String sql = "INSERT INTO Trip (truck_id, driver_id, start_date, end_date, status, commission) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trip.getTruckId());
            stmt.setInt(2, trip.getDriverId());
            stmt.setDate(3, new java.sql.Date(trip.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(trip.getEndDate().getTime()));
            stmt.setString(5, trip.getStatus());
            stmt.setDouble(6, trip.getCommission());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fetch all trips
    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM Trip";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Trip t = new Trip(
                        rs.getInt("id"),
                        rs.getInt("truck_id"),
                        rs.getInt("driver_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"),
                        rs.getDouble("commission"));
                trips.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return trips;
    }

    // Update commission
    public void updateCommission(int tripId, double commission) {
        String sql = "UPDATE Trip SET commission = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, commission);
            stmt.setInt(2, tripId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
