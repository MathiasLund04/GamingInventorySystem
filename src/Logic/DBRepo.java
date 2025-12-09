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
                    inventory.addItem(w);
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
                    inventory.addItem(a);
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
                    inventory.addItem(c);
                }


            }
        } catch (SQLException e) {
            e.printStackTrace(); //Exception for at se at forbindelsen virker og at det er skrevet ordentligt ind i koden
        }
        return inventory;
    }

    public  Item generateItem() throws Exception {
        try(Connection con = db.get()) {
            int times = rand;
            for (int i = 0; i < times; i++) {
                int choice = 2;
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

                        int newId = -1;
                        try{
                            newId = insertWeapon(1, weaponID);
                        } catch (NotEnoughInventorySpaceException e){
                            System.out.println("Error: " + e.getMessage());
                        } catch (TooMuchWeightException e){
                            System.out.println(e.getMessage());
                        } catch (SQLException e){
                            System.out.println(e.getMessage());
                        }
                        Weapon w = new Weapon(name, weaponType, rarity, weight, valuee, damage, handleType);
                        if(newId >-1){
                            w.setDbId(newId);
                        }
                        return w;

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

                            int newId = -1;
                            try{
                                newId = insertArmor(1, armorID);
                            } catch (NotEnoughInventorySpaceException e){
                                System.out.println("Error: " + e.getMessage());
                            } catch (TooMuchWeightException e){
                                System.out.println(e.getMessage());
                            } catch (SQLException e){
                                System.out.println(e.getMessage());
                            }

                        Armor a = new Armor(name, rarity, weight, valuee, durability, armorPlacement);
                            if(newId > 0){
                                a.setDbId(newId);
                            }
                            return a;
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

                            int newId = -1;
                            try {
                                newId = insertConsumable(1, consumableID);
                            } catch (NotEnoughInventorySpaceException e){
                                System.out.println("Error: " + e.getMessage());
                            } catch (TooMuchWeightException e){
                                System.out.println(e.getMessage());
                            } catch (SQLException e){
                                System.out.println(e.getMessage());
                            }
                            Consumable c = new Consumable(name, weight, valuee, description, consumableType);
                            if(newId > -1){
                                c.setDbId(newId);
                            }
                        }
                        break;
                }
            }
        }
        return null;
    }
    public int insertWeapon(int invId, int weaponId) throws Exception {
            String sqlw = "INSERT INTO hasWeapon(inventoryId, weaponId) VALUES (?,?)";
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
        String sqlc = "INSERT INTO hasConsumable(inventoryId, consumableId) VALUES (?,?)";
        try(Connection c = db.get()) {
            PreparedStatement ps = c.prepareStatement(sqlc, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invId);
            ps.setInt(2, consumableId);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    public int insertArmor(int invId, int armorId) throws Exception {
        String sqla = "INSERT INTO hasArmor(inventoryId, armorId) VALUES (?,?)";
        try(Connection c = db.get()) {
            PreparedStatement ps = c.prepareStatement(sqla, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, invId);
            ps.setInt(1,armorId);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return -1;
    }
}
