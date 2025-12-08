package Logic;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/locc_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER= "root";
    private static final String PASS= "Pande.Kage.04";

    public Connection get() throws Exception{
    return DriverManager.getConnection(URL,USER,PASS);}


}
