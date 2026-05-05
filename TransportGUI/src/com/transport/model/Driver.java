package com.transport.model;

public class Driver {
    private int id;
    private String name;
    private String phone;
    private String licenseNo;
    private String status;

    // Constructor
    public Driver(int id, String name, String phone, String licenseNo, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.licenseNo = licenseNo;
        this.status = status;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
