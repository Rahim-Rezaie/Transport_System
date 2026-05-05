# 🚛 Transport Management System

> A fully functional desktop application for managing transport operations — built with **Java Swing** and **MySQL** as a university database final project.

---

## 👋 What is this project?

Ever wondered how a transport company keeps track of all its trucks, drivers, trips, and money? That's exactly what this app does!

The **Transport Management System** is a desktop GUI application that lets you manage everything related to a transport/logistics business — from hiring drivers and assigning trucks to tracking trips, shipments, expenses, and invoices. Oh, and it has a powerful **global search** so you can look up any driver or truck in seconds! 🔍

This was built as a **final semester Database project** at university — combining everything we learned about SQL, Java, and software design.

---

## ✨ Features at a Glance

| Tab | What it does |
|-----|-------------|
| 🔍 **Search** | Search the entire system — find drivers, trucks, and trips instantly |
| 📊 **Dashboard** | Live overview — active trips, available trucks, unpaid invoices, total revenue |
| 👤 **Drivers** | Add and view all drivers with their license info and current status |
| 🚛 **Trucks** | Manage your truck fleet — add trucks, check availability |
| 🗺 **Trips** | Create and manage trips — assign drivers and trucks, track status |
| 📦 **Shipments** | Log shipments tied to each trip — description and weight |
| 💸 **Expenses** | Record trip expenses like fuel, tolls, maintenance, etc. |
| 🧾 **Invoices** | Generate and track invoices per trip — paid or unpaid |

---

## 🔍 The Global Search Feature (Our Favourite!)

Type anything into the search bar and the system searches **across the entire database** at once.

**Search by driver name** → you'll instantly see:
- 🟢 Is the driver currently active or available?
- 🗺 Which trip are they on right now?
- 🚛 Which truck are they driving?
- 💰 How much total commission have they earned?

**Search by truck number** → you'll see:
- The truck's current status
- Which driver is assigned to it
- The active trip dates

**Search by trip ID** → you'll see:
- Trip dates and status
- Assigned driver and truck
- Invoice amount and payment status

> Partial matching works! Type `"Ali"` and it finds `"Ali Khan"`, `"Syed Ali"`, everyone!

---

## 🛠 Tech Stack

| Technology | Why we used it |
|------------|---------------|
| ☕ **Java 17+** | Main programming language — object-oriented, cross-platform |
| 🖼 **Java Swing** | Built-in Java GUI framework for the desktop interface |
| 🐬 **MySQL** | Relational database to store all our data |
| 🔌 **JDBC** | Java Database Connectivity — connects Java to MySQL |
| 📦 **MySQL Connector/J 9.7.0** | The JDBC driver JAR file (in `/lib` folder) |

No external UI libraries needed — just pure Java and MySQL! 💪

---

## 🗄 Database Schema

The app uses a MySQL database called **`Transport_System`** with 6 tables:

```
Transport_System
│
├── 🚛 Truck          — id, number_plate, status
├── 👤 Driver         — id, name, phone, license_no, status
├── 🗺 Trip           — id, truck_id, driver_id, start_date, end_date, status, commission
├── 📦 Shipment       — id, trip_id, description, weight
├── 💸 Expense        — id, trip_id, type, amount
└── 🧾 Invoice        — id, trip_id, total_amount, status
```

**Relationships:**
- A Trip connects a Driver + a Truck
- Shipments, Expenses, and Invoices all belong to a Trip
- Everything links back through foreign keys 🔗

The SQL setup script is in `setup.sql` — just run it and your database is ready!

---

## 📁 Project Structure

```
TransportGUI/
│
├── lib/
│   └── mysql-connector-j-9.7.0.jar   ← JDBC driver (required!)
│
├── src/com/transport/
│   │
│   ├── dao/                           ← Database Access Objects (SQL lives here)
│   │   ├── DBConnection.java          ← MySQL connection settings
│   │   ├── DriverDAO.java
│   │   ├── TruckDAO.java
│   │   ├── TripDAO.java
│   │   ├── ShipmentDAO.java
│   │   ├── ExpenseDAO.java
│   │   ├── InvoiceDAO.java
│   │   └── SearchDAO.java             ← Multi-table JOIN queries for search
│   │
│   ├── model/                         ← Plain Java objects (one per table)
│   │   ├── Driver.java
│   │   ├── Truck.java
│   │   ├── Trip.java
│   │   ├── Shipment.java
│   │   ├── Expense.java
│   │   └── Invoice.java
│   │
│   └── ui/                            ← All the GUI panels
│       ├── MainFrame.java             ← Entry point — the main window with tabs
│       ├── SearchPanel.java           ← 🔍 Global search UI
│       ├── DashboardPanel.java        ← 📊 Live stats dashboard
│       ├── DriverPanel.java
│       ├── TruckPanel.java
│       ├── TripPanel.java
│       ├── ShipmentPanel.java
│       ├── ExpensePanel.java
│       ├── InvoicePanel.java
│       └── ManageTripDialog.java      ← Dialog for creating/managing trips
│
├── setup.sql                          ← Run this to create the database
└── sources.txt                        ← File list used for javac compilation
```

---

## ⚙️ How to Set Up & Run

### 1️⃣ Prerequisites
Make sure you have these installed:
- ✅ Java JDK 17 or higher → [Download](https://www.oracle.com/java/technologies/downloads/)
- ✅ MySQL Server → [Download](https://dev.mysql.com/downloads/mysql/)

---

### 2️⃣ Set up the database

Open MySQL and run the setup script:

```sql
source setup.sql;
```

This creates the `Transport_System` database and all 6 tables automatically. 🎉

---

### 3️⃣ Update your DB credentials

Open `src/com/transport/dao/DBConnection.java` and update these lines to match your MySQL setup:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/Transport_System";
private static final String USER = "root";          // ← your MySQL username
private static final String PASS = "your_password"; // ← your MySQL password
```

---

### 4️⃣ Compile the project

From the project root folder, run:

```bash
javac -cp "lib/mysql-connector-j-9.7.0.jar" -d src @sources.txt
```

---

### 5️⃣ Run the app!

```bash
java -cp "src:lib/mysql-connector-j-9.7.0.jar" com.transport.ui.MainFrame
```

> **Windows users:** replace the `:` with `;` in the classpath:
> ```bash
> java -cp "src;lib/mysql-connector-j-9.7.0.jar" com.transport.ui.MainFrame
> ```

---

## 🏗 Architecture — How it All Fits Together

We used the **3-Layer Architecture** (also called MVC-style):

```
┌─────────────────────────────┐
│         UI Layer            │  ← What you see (Swing panels)
│  SearchPanel, DriverPanel…  │
└──────────────┬──────────────┘
               │ calls
┌──────────────▼──────────────┐
│         DAO Layer           │  ← Talks to the database
│  DriverDAO, TripDAO…        │
└──────────────┬──────────────┘
               │ SQL queries
┌──────────────▼──────────────┐
│        MySQL Database       │  ← Stores everything permanently
│      Transport_System       │
└─────────────────────────────┘
```

The **Model** classes (Driver.java, Trip.java, etc.) carry data between layers — like little data containers 📦.

---

## 👥 Team & Collaboration

This project was built as part of the **Database course final project** (2nd Semester).

| Role | What we did |
|------|------------|
| 🗄 Database Design | Designed the schema, relationships, and SQL queries |
| ☕ Backend (DAO) | Wrote all JDBC queries connecting Java to MySQL |
| 🖼 Frontend (UI) | Built all Swing panels and the GUI layout |
| 🔍 Search Feature | Implemented multi-table JOIN queries + search panel |

---

## 📝 What We Learned

- How to design a **relational database** with proper foreign keys
- How **JDBC** works to connect Java to a MySQL database
- How to build **desktop GUIs** with Java Swing
- How to use **JOIN queries** to pull data from multiple tables at once
- How to apply **3-layer architecture** to keep code clean and organised
- Using **SwingWorker** for background threads so the UI never freezes
- **Debugging** real SQL errors from real database column mismatches (it happens to everyone! 😅)

---

## 🐛 Known Limitations

- No login/authentication system (yet!)
- The UI theme is dark but not fully responsive on very small screens
- Deleting records is not yet implemented — only adding and viewing
- No export to PDF/Excel (would be a nice future feature!)

---

## 🚀 Future Ideas

- [ ] 🔐 Add a login screen with user roles (Admin / Viewer)
- [ ] 📊 Charts and graphs on the dashboard
- [ ] 📄 Export reports to PDF
- [ ] 🗑 Edit and delete records
- [ ] 🌐 Move to a web-based version

---

## 📜 License

This project is for **educational purposes** as part of a university course.  
Feel free to use it as a reference or learning resource! 🎓

---

<div align="center">

Made with ☕ Java · 🐬 MySQL · ❤️ and a lot of debugging by Rahim Rezaie

</div>
