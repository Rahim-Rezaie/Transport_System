package com.transport.model;

public class Truck {
    private int id;
    private String number;
    private String status;

    public Truck(int id, String number, String status) {
        this.id = id;
        this.number = number;
        this.status = status;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
