package Adventurer;

import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;
import Logic.DBConnection;
import Logic.DBRepo;
import Logic.GameLogic;
import Logic.Inventory.Inventory;

public class Adventurer {
    private DBConnection db;
    private DBRepo dbRepo;
    private Inventory inv;
    private GameLogic gameLogic;

public Adventurer() {
    this.db = new DBConnection();
    this.dbRepo = new DBRepo(db);
    this.inv = new Inventory(0,16,192,32,50,32, 0);
    this.gameLogic = new GameLogic(inv, dbRepo);

}

    public Adventurer(Inventory inventory, DBRepo dbRepo) {
        this.db = new DBConnection();
        this.dbRepo = dbRepo != null ? dbRepo : new DBRepo(db);
        this.inv = inventory != null ? inventory : new Inventory(0,16,192,32,50,32, 0);
        this.gameLogic = new GameLogic(inv, this.dbRepo);
    }

    public String goOnAdventure() throws Exception {
        int addedCoins = generateCoins();
        inv.setCoins(inv.getCoins() + addedCoins);
        dbRepo.insertCoins(inv.getCoins());
        String msg = "You found " + addedCoins + " coins on your adventure!";

        DBRepo.GeneratedItem gen = dbRepo.generateItem();
        if (gen == null || gen.item == null) {
            return "No item generated.";
        }
        Item item = gen.item;

        int newId = -1;
        if (gen.category.equals("weapon") && (item instanceof Weapon)) {
            newId = dbRepo.insertWeapon(1, gen.templateId);
        } else if (gen.category.equals("armor") && (item instanceof Armor)) {
            newId = dbRepo.insertArmor(1, gen.templateId);
        } else if (gen.category.equals("consumable") && (item instanceof Consumable)) {
            newId = dbRepo.insertConsumable(1, gen.templateId);
        }

        // checker hvis item er conumable og allerade er der, så stacker den
        if (item instanceof Consumable) {
            for (Item i : inv.getSlots()) {
                if (i instanceof Consumable) {
                    Consumable existing = (Consumable) i;
                    if (existing.getName().equals(item.getName())) {
                        existing.consumableCount++;
                        inv.setTotalWeight((int) inv.calculateTotalWeight());
                        return existing.getName() + " stacked to " + existing.getConsumableCount();
                    }
                }
            }
        }
        if (!inv.addItemCheck(item)) {
            return "Not enough room or carry capacity to add item";
        }


        if (newId > 0) {
            item.setDbId(newId);
        }

        String added = inv.addItem(item);
        return msg + "\n" + added;
    }

    public int generateCoins(){
        int rand = (int)(Math.random() * 10) + 1;
        return rand;
    }


}
