package com.transport.model;

public class Shipment {
    private int id;
    private int tripId;
    private String description;
    private double weight;

    public Shipment(int id, int tripId, String description, double weight) {
        this.id = id;
        this.tripId = tripId;
        this.description = description;
        this.weight = weight;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public int getTripId() {
        return tripId;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }
}
