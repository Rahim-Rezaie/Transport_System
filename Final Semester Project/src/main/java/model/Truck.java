public class Truck {
    private int id; private String numberPlate; private String type; private String status;
    public Truck(int id,String np,String type,String status){
        this.id=id; this.numberPlate=np; this.type=type; this.status=status;
    }
    public int getId(){ return id; }
    public String getNumberPlate(){ return numberPlate; }
    public String getType(){ return type; }
    public String getStatus(){ return status; }
}
