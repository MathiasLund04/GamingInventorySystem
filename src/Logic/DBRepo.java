package Logic;

import Enums.Rarity;
import Enums.WeaponHandleType;
import Enums.WeaponType;
import Enums.ArmorPlacement;
import Enums.ConsumableType;
import Exceptions.NotEnoughInventorySpaceException;
import Exceptions.TooMuchWeightException;
import Items.Armor;
import Items.Weapon;
import Items.Consumable;
import Logic.Inventory.Inventory;

import java.sql.*;

public class DBRepo {
    private final DBConnection db;
    int rand = (int) (Math.random() * 3);
    public DBRepo(DBConnection db) {
        this.db = db;
    }

    //Test af forbindelse
    public void testConnection() {
        try (Connection c = db.get()) {
            DatabaseMetaData md = c.getMetaData();
            System.out.println("✅ Forbindelse OK: " + md.getURL());
            System.out.println("    Driver: " + md.getDriverName() + " - " + md.getDriverVersion());

        } catch (Exception e) {
            System.out.println("❌ Forbindelse FEJL: " + e.getMessage());
            System.out.println("Tip: Tjek URL/USER/PASS og at MySQL kører.");
        }
    }

    
    //Metode til at hente inventory fra DB
    public Inventory loadInventory() throws Exception {
        Inventory inventory = null;

        try (Connection conn = db.get()){
            //Inventory (antal coins, slots og hvor meget det hele vejer)
            String sqlInventory = "SELECT coins, totalWeight, unlockedSlots FROM Inventory WHERE inventoryId = 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlInventory);
                 ResultSet rs = ps.executeQuery()) {
                
                if (rs.next()){
                    int coins = rs.getInt("coins");
                    int totalWeight = rs.getInt("totalWeight");
                    int unlockedSlots = rs.getInt("unlockedSlots");

                    inventory = new Inventory(coins, 16, 192, unlockedSlots, 50, 32, totalWeight);
                    //Inventory data er gemt i DB men den laver en ny for at kunne vise det til spilleren
                }
            }

            //Weapons (Ændrer String-navnet for at vise hvilken query der hører til hvilket item)
            String sqlWeapons = "SELECT w.name, w.weaponType, w.rarity, w.weight, w.valuee, w.damage, w.handleType, hw.id AS hw_id " +
                    "FROM HasWeapon hw " +
                    "JOIN Weapon w ON w.weaponId = hw.weaponId " +
                    "WHERE hw.inventoryId = 1"; //Da vi kun har 1 adventurer, er inventoryId sat til 1 (Kan ændres senere)
            try (PreparedStatement ps = conn.prepareStatement(sqlWeapons);
                 ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    Weapon w = new Weapon(
                            rs.getString("name"),
                            WeaponType.valueOf(rs.getString("weaponType")),
                            Rarity.valueOf(rs.getString("rarity")),
                            rs.getDouble("weight"),
                            rs.getInt("valuee"),
                            rs.getInt("damage"),
                            WeaponHandleType.valueOf(rs.getString("handleType"))
                            );
                    w.setDbId(rs.getInt("hw_id")); //Dette bruges til at kunne blande items så det ikke kun er sat efter Weapon, Armor og Consumable
                    inventory.addItemCheck(w);
                }
            }

            //Armor
            String sqlArmor = "SELECT a.name, a.rarity, a.weight, a.valuee, a.durability, a.armorPlacement, ha.id AS ha_id " +
                    "FROM HasArmor ha " +
                    "JOIN Armor a ON a.armorId = ha.armorId " +
                    "WHERE ha.inventoryId = 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlArmor);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Armor a = new Armor(
                            rs.getString("name"),
                            Rarity.valueOf(rs.getString("rarity")),
                            rs.getDouble("weight"),
                            rs.getInt("valuee"),
                            rs.getInt("durability"),
                            ArmorPlacement.valueOf(rs.getString("armorPlacement"))
                    );
                    a.setDbId(rs.getInt("ha_id"));
                    inventory.addItemCheck(a);
                }
            }

            //Consumables
            String sqlConsumables = "SELECT c.name, c.weight, c.valuee, c.description, c.consumableType, hc.id AS hc_id " +
                    "FROM HasConsumable hc " +
                    "JOIN Consumable c ON c.consumableId = hc.consumableId " +
                    "WHERE hc.inventoryId = 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlConsumables);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Consumable c = new Consumable(
                            rs.getString("name"),
                            rs.getDouble("weight"),
                            rs.getInt("valuee"),
                            rs.getString("description"),
                            ConsumableType.valueOf(rs.getString("consumableType"))
                    );
                    c.setDbId(rs.getInt("hc_id"));
                    inventory.addItemCheck(c);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace(); //Exception for at se at forbindelsen virker og at det er skrevet ordentligt ind i koden
        }
        return inventory;
    }

    public int generateItem() throws Exception {
        try(Connection c = db.get()) {
            int times = rand;
            int id = 0;
            for (int i = 0; i < times; i++) {
                int choice = 1;
                switch (choice) {
                    case 1:
                        String sql = "Select * from weapon ORDER BY Rand() Limit 1\n";
                        PreparedStatement ps = c.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        System.out.println("name | weaponType | rarity | weight | value | dmg");
                        while(rs.next()){
                        int weaponID = rs.getInt("weaponID");
                        String name =rs.getString("name");
                        String weapontype = rs.getString("weapontype");
                        String rarity = rs.getString("rarity");
                        double weight = rs.getDouble("weight");
                        int valuee = rs.getInt("valuee");
                        int damage = rs.getInt("damage");
                        System.out.printf("%s | %s | %s | %.1f | %d | %d%n ", name , weapontype, rarity, weight, valuee, damage);
                        id = weaponID;
                        }
                        try{
                            insert();
                        } catch (NotEnoughInventorySpaceException){

                        } catch (TooMuchWeightException){
                            
                        }


                    break;
                    case 2:
                        String sql4 = "Select * from armor ORDER BY Rand() Limit 1\n";
                        PreparedStatement ps4 = c.prepareStatement(sql4);
                        ResultSet rs1 = ps4.executeQuery();
                        System.out.println("name | rarity | weight | value | durability");
                        while (rs1.next()) {
                            int armorID = rs1.getInt("armorID");
                            String name = rs1.getString("name");
                            String rarity = rs1.getString("rarity");
                            double weight = rs1.getDouble("weight");
                            int valuee = rs1.getInt("valuee");
                            int durability =  rs1.getInt("durability");
                            System.out.printf("%s | %s | %.1f | %d | %d%n", name, rarity, weight, valuee, durability);
                        id = armorID;
                        }
                        break;
                    case 3:
                        String sql5 = "Select * from consumable ORDER BY Rand() Limit 1\n";
                        PreparedStatement ps2 = c.prepareStatement(sql5);
                        ResultSet rs2 = ps2.executeQuery();
                        while (rs2.next()) {
                            int consumableID = rs2.getInt("consumableID");
                            String name = rs2.getString("name");
                            double weight = rs2.getDouble("weight");
                            int valuee = rs2.getInt("valuee");
                            String description = rs2.getString("description");
                            System.out.printf("%s | %.1f | %d | %s", name, weight, valuee, description);
                            id = consumableID;
                        }
                        break;
                }
            }
        return id;
        }
    }

    public void insert() throws Exception {
        try(Connection c = db.get()) {
            String sql = "INSERT INTO inventory(inventoryId) VALUES (?)";
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, generateItem());
            ps.executeUpdate();
            System.out.println("Item added");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("Indsat id = " + newId);
                }
            }
        }
    }
}
