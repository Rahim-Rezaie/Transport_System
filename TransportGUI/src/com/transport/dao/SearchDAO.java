package com.transport.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchDAO {

    /**
     * Search drivers by name (partial match, case-insensitive).
     * Returns: driver_id, driver_name, phone, license_no, driver_status,
     *          trip_id, trip_status, start_date, end_date,
     *          truck_number, total_commission
     */
    public List<Object[]> searchDrivers(String keyword) {
        List<Object[]> results = new ArrayList<>();
        String sql =
            "SELECT d.id, d.name, d.phone, d.license_no, d.status AS driver_status, " +
            "       t.id AS trip_id, t.status AS trip_status, " +
            "       t.start_date, t.end_date, " +
            "       tk.number_plate AS truck_number, " +
            "       COALESCE(SUM(t.commission), 0) AS total_commission " +
            "FROM Driver d " +
            "LEFT JOIN Trip t  ON d.id = t.driver_id " +
            "LEFT JOIN Truck tk ON t.truck_id = tk.id " +
            "WHERE d.name LIKE ? " +
            "GROUP BY d.id, d.name, d.phone, d.license_no, d.status, " +
            "         t.id, t.status, t.start_date, t.end_date, tk.number_plate " +
            "ORDER BY d.name, t.start_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("license_no"),
                    rs.getString("driver_status"),
                    rs.getObject("trip_id") != null ? rs.getInt("trip_id") : "—",
                    rs.getString("trip_status") != null ? rs.getString("trip_status") : "No Active Trip",
                    rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : "—",
                    rs.getDate("end_date") != null ? rs.getDate("end_date").toString() : "—",
                    rs.getString("truck_number") != null ? rs.getString("truck_number") : "—",
                    String.format("$%.2f", rs.getDouble("total_commission"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                "Search Error: " + e.getMessage(), "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return results;
    }

    /**
     * Search trucks by number plate (partial match).
     * Returns: truck_id, number_plate, truck_status,
     *          driver_name, trip_id, trip_status, start_date, end_date
     */
    public List<Object[]> searchTrucks(String keyword) {
        List<Object[]> results = new ArrayList<>();
        String sql =
            "SELECT tk.id, tk.number_plate, tk.status AS truck_status, " +
            "       d.name AS driver_name, " +
            "       t.id AS trip_id, t.status AS trip_status, " +
            "       t.start_date, t.end_date " +
            "FROM Truck tk " +
            "LEFT JOIN Trip t  ON tk.id = t.truck_id AND t.status = 'Active' " +
            "LEFT JOIN Driver d ON t.driver_id = d.id " +
            "WHERE tk.number_plate LIKE ? " +
            "ORDER BY tk.number_plate";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("number_plate"),
                    rs.getString("truck_status"),
                    rs.getString("driver_name") != null ? rs.getString("driver_name") : "—",
                    rs.getObject("trip_id") != null ? rs.getInt("trip_id") : "—",
                    rs.getString("trip_status") != null ? rs.getString("trip_status") : "Not on Trip",
                    rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : "—",
                    rs.getDate("end_date") != null ? rs.getDate("end_date").toString() : "—"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                "Search Error: " + e.getMessage(), "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return results;
    }

    /**
     * Search trips by trip ID or driver name (partial match).
     * Returns: trip_id, trip_status, start_date, end_date, commission,
     *          driver_name, truck_number, invoice_amount, invoice_status
     */
    public List<Object[]> searchTrips(String keyword) {
        List<Object[]> results = new ArrayList<>();
        String sql =
            "SELECT t.id, t.status AS trip_status, t.start_date, t.end_date, t.commission, " +
            "       d.name AS driver_name, " +
            "       tk.number_plate AS truck_number, " +
            "       COALESCE(i.total_amount, 0) AS invoice_amount, " +
            "       COALESCE(i.status, '—') AS invoice_status " +
            "FROM Trip t " +
            "LEFT JOIN Driver d  ON t.driver_id = d.id " +
            "LEFT JOIN Truck tk  ON t.truck_id = tk.id " +
            "LEFT JOIN Invoice i ON t.id = i.trip_id " +
            "WHERE CAST(t.id AS CHAR) LIKE ? OR d.name LIKE ? " +
            "ORDER BY t.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("trip_status"),
                    rs.getDate("start_date") != null ? rs.getDate("start_date").toString() : "—",
                    rs.getDate("end_date") != null ? rs.getDate("end_date").toString() : "—",
                    String.format("$%.2f", rs.getDouble("commission")),
                    rs.getString("driver_name") != null ? rs.getString("driver_name") : "—",
                    rs.getString("truck_number") != null ? rs.getString("truck_number") : "—",
                    String.format("$%.2f", rs.getDouble("invoice_amount")),
                    rs.getString("invoice_status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null,
                "Search Error: " + e.getMessage(), "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return results;
    }
}
