public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Transport_System";
    private static final String USER = "root";
    private static final String PASS = "password";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
