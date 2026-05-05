package com.transport.model;

public class Expense {
    private int id;
    private int tripId;
    private String type;
    private double amount;

    public Expense(int id, int tripId, String type, double amount) {
        this.id = id;
        this.tripId = tripId;
        this.type = type;
        this.amount = amount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getTripId() {
        return tripId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
