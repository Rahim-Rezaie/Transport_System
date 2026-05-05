public class TruckDAO {
    public static List<Truck> getAll(){
        List<Truck> list = new ArrayList<>();
        try(Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Truck")){
            while(rs.next()){
                Truck t = new Truck(rs.getInt("id"), rs.getString("number_plate"), rs.getString("type"), rs.getString("status"));
                list.add(t);
            }
        }catch(Exception e){ e.printStackTrace(); }
        return list;
    }
}
