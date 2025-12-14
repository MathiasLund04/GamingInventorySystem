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

    public StringBuilder goOnAdventure() throws Exception {
        StringBuilder msg = new StringBuilder();
        int addedCoins = generateCoins();
        inv.setCoins(inv.getCoins() + addedCoins);
        dbRepo.updateCoins(inv.getCoins());
        msg.append("You found " + addedCoins + " coins on your adventure! \n");


        DBRepo.GeneratedItem gen = dbRepo.generateItem();

        if (gen == null || gen.item == null) {
            return msg.append("No item generated.");
        }
        Item item = gen.item;

        int newId = -1;
        if (item instanceof Consumable) {
            for (Item i : inv.getSlots()) {
                if (i instanceof Consumable) {
                    Consumable existing = (Consumable) i;
                    if (existing.getName().equals(item.getName())) {
                        try {
                            int hasId = dbRepo.insertConsumable(1, gen.templateId);
                            if (hasId > 0) {
                                existing.setDbId(hasId);
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        existing.increaseQuantity(1);
                        inv.setTotalWeight((int) inv.calculateTotalWeight());
                        return msg.append(existing.getName() + " stacked to " + existing.getConsumableCount());
                    }
                }
            }
            if (!inv.addItemCheck(item)) {
                return msg.append("Not enough Room or carry capacity to add Item.");
            }
            newId = dbRepo.insertConsumable(1, gen.templateId);
            if (newId > 0) {
                item.setDbId(newId);
            }
            msg.append(inv.addItem(item));
            return msg;
        }


        if (gen.category.equals("weapon") && (item instanceof Weapon)) {
            newId = dbRepo.insertWeapon(1, gen.templateId);
        } else if (gen.category.equals("armor") && (item instanceof Armor)) {
            newId = dbRepo.insertArmor(1, gen.templateId);

            // checker hvis item er conumable og allerade er der, så stacker den
            if (!inv.addItemCheck(item)) {
                return msg.append("Not enough room or carry capacity to add item");
            }
            if (newId > 0) {
                item.setDbId(newId);
            }
            msg.append(inv.addItem(item));
        }
            return msg;
    }


    public int generateCoins(){
        int rand = (int)(Math.random() * 10) + 1;
        return rand;
    }


}
