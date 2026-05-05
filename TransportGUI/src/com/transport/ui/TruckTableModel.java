package com.transport.ui;

import com.transport.dao.TruckDAO;
import com.transport.model.Truck;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TruckTableModel extends AbstractTableModel {
    private List<Truck> trucks;
    public TruckTableModel(){ trucks = new TruckDAO().getAllTrucks(); }
    public int getRowCount(){ return trucks.size(); }
    public int getColumnCount(){ return 3; }
    public Object getValueAt(int r,int c){
        Truck t = trucks.get(r);
        switch(c){
            case 0: return t.getId();
            case 1: return t.getNumber();
            case 2: return t.getStatus();
        }
        return null;
    }
}
