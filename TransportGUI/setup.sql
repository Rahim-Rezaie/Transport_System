CREATE DATABASE IF NOT EXISTS Transport_System;
USE Transport_System;

CREATE TABLE IF NOT EXISTS Truck (
    id INT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Driver (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    license_no VARCHAR(50),
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Trip (
    id INT AUTO_INCREMENT PRIMARY KEY,
    truck_id INT,
    driver_id INT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    commission DECIMAL(10,2) DEFAULT 0.0,
    FOREIGN KEY (truck_id) REFERENCES Truck(id),
    FOREIGN KEY (driver_id) REFERENCES Driver(id)
);

CREATE TABLE IF NOT EXISTS Shipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    trip_id INT,
    description VARCHAR(255),
    weight DECIMAL(10,2),
    FOREIGN KEY (trip_id) REFERENCES Trip(id)
);

CREATE TABLE IF NOT EXISTS Expense (
    id INT AUTO_INCREMENT PRIMARY KEY,
    trip_id INT,
    type VARCHAR(50),
    amount DECIMAL(10,2),
    FOREIGN KEY (trip_id) REFERENCES Trip(id)
);

CREATE TABLE IF NOT EXISTS Invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    trip_id INT,
    amount DECIMAL(10,2),
    status VARCHAR(50),
    FOREIGN KEY (trip_id) REFERENCES Trip(id)
);
