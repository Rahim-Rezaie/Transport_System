public class MainFrame extends JFrame {
    public MainFrame(){
        setTitle("Transport System");
        setSize(1000,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMenuBar menu = new JMenuBar();
        JMenu manage = new JMenu("Manage");
        JMenuItem trucks = new JMenuItem("Trucks");
        trucks.addActionListener(e -> setContentPane(new TruckPanel()));
        manage.add(trucks);
        menu.add(manage);
        setJMenuBar(menu);
        setVisible(true);
    }
    public static void main(String[] args){ new MainFrame(); }
}
