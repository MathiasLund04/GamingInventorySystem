package Logic;

public class DBRepo {
    private final DBConnection db;
    public DBRepo(DBConnection db) {
        this.db = db;
    }

    public void printAll() {
        String sql = "SELECT name, weight, valuee  FROM Inventory ORDER BY id DESC";
    }



}
