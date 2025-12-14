package Logic;

import Enums.Rarity;
import Enums.WeaponHandleType;
import Enums.WeaponType;
import Enums.ArmorPlacement;
import Enums.ConsumableType;
import Exceptions.NotEnoughInventorySpaceException;
import Exceptions.TooMuchWeightException;
import Items.Armor;
import Items.Item;
import Items.Weapon;
import Items.Consumable;
import Logic.Inventory.Inventory;


import java.sql.*;
import java.util.List;

public class DBRepo {
    private final DBConnection db;
    public DBRepo(DBConnection db) {
        this.db = db;
    }

    public static class GeneratedItem {
        public final Item item;
        public final int templateId;
        public final String category;

        public GeneratedItem(Item item, int templateId, String category) {
            this.item = item;
            this.templateId = templateId;
            this.category = category;
        }
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
    public Inventory loadInventory(Inventory inventory) throws Exception {


        try (Connection conn = db.get()){
            //Inventory (antal coins, slots og hvor meget det hele vejer)
            String sqlInventory = "SELECT coins, totalWeight, unlockedSlots FROM Inventory WHERE inventoryId = 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlInventory);
                 ResultSet rs = ps.executeQuery()) {
                
                if (rs.next()){
                    int coins = rs.getInt("coins");
                    int totalWeight = rs.getInt("totalWeight");
                    int unlockedSlots = rs.getInt("unlockedSlots");

                    //Opdatere inventory
                    inventory.setCoins(coins);
                    inventory.setUnlockedSlots(unlockedSlots);
                    inventory.setTotalWeight(totalWeight);
                }
            }

            inventory.clearItems();

            //Weapons (Ændrer String-navnet for at vise hvilken query der hører til hvilket item)
            String sqlWeapons = "SELECT w.name, w.weaponType, w.rarity, w.weight, w.valuee, w.damage, w.handleType, hw.id AS hw_id " +
                    "FROM Hasitem hw " +
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
                    inventory.addItem(w);
                }
            }

            //Armor
            String sqlArmor = "SELECT a.name, a.rarity, a.weight, a.valuee, a.durability, a.armorPlacement, ha.id AS ha_id " +
                    "FROM Hasitem ha " +
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
                    inventory.addItem(a);
                }
            }

            //Consumables
            String sqlConsumables = "SELECT c.name, c.weight, c.valuee, c.description, c.consumableType, hc.quantity, hc.id AS hc_id " +
                    "FROM Hasitem hc " +
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
                    c.setConsumableCount(rs.getInt("quantity"));
                    inventory.addItem(c);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace(); //Exception for at se at forbindelsen virker og at det er skrevet ordentligt ind i koden
        }
        return inventory;
    }

    public  GeneratedItem generateItem() throws Exception {
        try(Connection con = db.get()) {
                int choice = 3;
                switch (choice) {
                    case 1:
                        String sql = "Select * from weapon ORDER BY Rand() Limit 1\n";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        while(rs.next()){
                        int weaponID = rs.getInt("weaponID");
                        String name =rs.getString("name");
                        WeaponType weaponType = WeaponType.valueOf(rs.getString("weaponType"));
                        Rarity rarity = Rarity.valueOf(rs.getString("rarity"));
                        double weight = rs.getDouble("weight");
                        int valuee = rs.getInt("valuee");
                        int damage = rs.getInt("damage");
                        WeaponHandleType handleType = WeaponHandleType.valueOf(rs.getString("handletype"));
                        Weapon w = new Weapon(name, weaponType, rarity, weight, valuee, damage, handleType);
                        return new GeneratedItem(w,weaponID,"weapon");

                        }
                    break;

                    case 2:
                        String sql2 = "Select * from armor ORDER BY Rand() Limit 1\n";
                        PreparedStatement ps2 = con.prepareStatement(sql2);
                        ResultSet rs2 = ps2.executeQuery();
                        System.out.println("name | rarity | weight | value | durability");
                        while (rs2.next()) {
                            int armorID = rs2.getInt("armorID");
                            String name = rs2.getString("name");
                            Rarity rarity = Rarity.valueOf(rs2.getString("rarity"));
                            double weight = rs2.getDouble("weight");
                            int valuee = rs2.getInt("valuee");
                            int durability =  rs2.getInt("durability");
                            ArmorPlacement armorPlacement = ArmorPlacement.valueOf(rs2.getString("armorPlacement"));
                            System.out.printf("%s | %s | %.1f | %d | %d%n", name, rarity, weight, valuee, durability);

                        Armor a = new Armor(name, rarity, weight, valuee, durability, armorPlacement);
                            return new GeneratedItem(a,armorID,"armor");
                        }
                        break;
                    case 3:
                        String sql3 = "Select * from consumable ORDER BY Rand() Limit 1\n";
                        PreparedStatement ps3 = con.prepareStatement(sql3);
                        ResultSet rs3 = ps3.executeQuery();
                        while (rs3.next()) {
                            int consumableID = rs3.getInt("consumableID");
                            String name = rs3.getString("name");
                            double weight = rs3.getDouble("weight");
                            int valuee = rs3.getInt("valuee");
                            String description = rs3.getString("description");
                            ConsumableType consumableType = ConsumableType.valueOf(rs3.getString("consumableType"));
                            System.out.printf("%s | %.1f | %d | %s", name, weight, valuee, description);

                            Consumable c = new Consumable(name, weight, valuee, description, consumableType);
                            return new GeneratedItem(c,consumableID,"consumable");
                        }
                        break;
                }
            }
        return null;
    }
    public int insertWeapon(int invId, int weaponId) throws Exception {
            String sqlw = "INSERT INTO hasitem(inventoryId, weaponId) VALUES (?,?)";
        try(Connection c = db.get()) {
            PreparedStatement ps = c.prepareStatement(sqlw, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invId);
            ps.setInt(2, weaponId);
            ps.executeUpdate();
            System.out.println("Item added");
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("Indsat id = " + newId);
                    return newId;
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return -1;
    }
    public int insertConsumable(int invId, int consumableId) throws Exception {
        try(Connection c = db.get()) {
        String updateSql = "UPDATE Hasitem SET quantity = quantity + 1 WHERE id = ? AND consumableId = ? AND quantity >= 1 AND quantity <?";
            try (PreparedStatement ps = c.prepareStatement(updateSql)) {
                ps.setInt(1, invId);
                ps.setInt(2, consumableId);
                ps.setInt(3, Inventory.getMaxStack());
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    String selectSql = "SELECT id FROM Hasitem WHERE inventoryId = ? AND consumableId = ?";
                    try (PreparedStatement ps2 = c.prepareStatement(selectSql)) {
                        ps2.setInt(1, invId);
                        ps2.setInt(2, consumableId);
                        try (ResultSet rs = ps2.executeQuery()) {
                            if (rs.next()) {
                                return rs.getInt(1);
                            }
                        }
                    }
                } else {
                    String selectExist = "SELECT id FROM Hasitem WHERE inventoryId = ? AND consumableId = ?";
                    try (PreparedStatement psExist = c.prepareStatement(selectExist)) {
                        psExist.setInt(1, invId);
                        psExist.setInt(2, consumableId);
                        try (ResultSet rs = psExist.executeQuery()) {
                            if (rs.next()) {
                                // item exists but quantity == maxStack, return its id (no increment)
                                return rs.getInt(1);
                            }
                        }
                    }
                    String insertSql = "INSERT INTO hasitem(inventoryId, consumableId, quantity) VALUES (?,?,1)";
                    try (PreparedStatement ps3 = c.prepareStatement(insertSql,Statement.RETURN_GENERATED_KEYS)) {
                    ps3.setInt(1, invId);
                    ps3.setInt(2, consumableId);
                    ps3.executeUpdate();
                    try (ResultSet rs = ps3.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getInt(1);
                            }
                        }
                       }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return -1;
    }
    public int insertArmor(int invId, int armorId) throws Exception {
        String sqla = "INSERT INTO hasitem(inventoryId, armorId) VALUES (?,?)";
        try(Connection c = db.get()) {
            PreparedStatement ps = c.prepareStatement(sqla, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invId);
            ps.setInt(2,armorId);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    System.out.println("Indsat id = " + newId);
                    return newId;
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return -1;
    }


    public boolean deleteWeapon(int hasId) throws Exception {
        String sql = "DELETE FROM hasitem WHERE id = ?";
        try (Connection c = db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hasId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
    public boolean deleteArmor(int hasId) throws Exception {
        String sql = "DELETE FROM hasitem WHERE id = ?";
        try (Connection c = db.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hasId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
    public boolean deleteConsumable(int hasId) throws Exception {
        try (Connection conn = db.get()) {
            String decSql = "UPDATE Hasitem SET quantity = quantity - 1 WHERE id = ? AND quantity > 1";
            try (PreparedStatement ps = conn.prepareStatement(decSql)) {
                ps.setInt(1, hasId);
                int decUpdated = ps.executeUpdate();
                if (decUpdated > 0) {
                    return true;
                }
            }
            String delSql = "DELETE FROM Hasitem WHERE id = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(delSql)) {
                ps2.setInt(1, hasId);
                int deleted = ps2.executeUpdate();
                return deleted > 0;
            }
        }
    }

    public void updateCoins(int coins) throws Exception {
        String sql = "UPDATE Inventory SET coins = ? WHERE inventoryId = 1;";

        try (Connection c = db.get();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,coins);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
