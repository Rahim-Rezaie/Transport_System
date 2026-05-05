public class TruckTableModel extends AbstractTableModel {
    private List<Truck> trucks;
    public TruckTableModel(){ trucks = TruckDAO.getAll(); }
    public int getRowCount(){ return trucks.size(); }
    public int getColumnCount(){ return 4; }
    public Object getValueAt(int r,int c){
        Truck t = trucks.get(r);
        switch(c){
            case 0: return t.getId();
            case 1: return t.getNumberPlate();
            case 2: return t.getType();
            case 3: return t.getStatus();
        }
        return null;
    }
}
