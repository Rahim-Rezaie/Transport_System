public class TruckPanel extends JPanel {
    private JTable table;
    public TruckPanel(){
        setLayout(new BorderLayout());
        table = new JTable(new TruckTableModel());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
