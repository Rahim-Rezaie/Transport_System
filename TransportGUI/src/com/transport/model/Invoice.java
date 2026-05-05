package com.transport.model;

public class Invoice {
    private int id;
    private int tripId;
    private double amount;
    private String status;

    public Invoice(int id, int tripId, double amount, String status) {
        this.id = id;
        this.tripId = tripId;
        this.amount = amount;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public int getTripId() { return tripId; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
}
