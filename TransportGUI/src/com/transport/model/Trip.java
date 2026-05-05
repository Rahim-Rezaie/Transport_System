package com.transport.model;

import java.util.Date;

public class Trip {
    private int id;
    private int truckId;
    private int driverId;
    private Date startDate;
    private Date endDate;
    private String status;

    private double commission;

    public Trip(int id, int truckId, int driverId, Date startDate, Date endDate, String status, double commission) {
        this.id = id;
        this.truckId = truckId;
        this.driverId = driverId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.commission = commission;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTruckId() { return truckId; }
    public void setTruckId(int truckId) { this.truckId = truckId; }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCommission() { return commission; }
    public void setCommission(double commission) { this.commission = commission; }
}
