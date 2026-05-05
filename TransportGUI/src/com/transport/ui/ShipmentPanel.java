package com.transport.ui;

import com.transport.dao.ShipmentDAO;
import com.transport.model.Shipment;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShipmentPanel extends JPanel {
    private JTable table;
    private ShipmentDAO shipmentDAO;

    public ShipmentPanel() {
        shipmentDAO = new ShipmentDAO();
        setLayout(new BorderLayout());

        String[] columns = { "ID", "Trip ID", "Description", "Weight" };
        List<Shipment> shipments = shipmentDAO.getAllShipments();
        Object[][] data = new Object[shipments.size()][4];
        for (int i = 0; i < shipments.size(); i++) {
            Shipment s = shipments.get(i);
            data[i][0] = s.getId();
            data[i][1] = s.getTripId();
            data[i][2] = s.getDescription();
            data[i][3] = s.getWeight();
        }
        table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
