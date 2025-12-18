package Adventurer;

import Exceptions.NotEnoughInventorySpaceException;
import Exceptions.TooMuchWeightException;
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
    private GameLogic gl;


    public Adventurer(Inventory inventory, DBRepo dbRepo) {
        this.db = new DBConnection();
        this.dbRepo = dbRepo != null ? dbRepo : new DBRepo(db);
        this.inv = inventory != null ? inventory : new Inventory(0,32,0);
        this.gl = new GameLogic(inv);
    }
    public StringBuilder goOnAdventure() throws Exception {
        StringBuilder msg = new StringBuilder();
        int addedCoins = generateCoins();
        inv.setCoins(inv.getCoins() + addedCoins);
        dbRepo.updateCoins(inv.getCoins());
        if (addedCoins == 1){
            msg.append("\nYou found " + addedCoins + " coin on your adventure! \n");
        } else {
            msg.append("\nYou found " + addedCoins + " coins on your adventure! \n");;
        }


        DBRepo.GeneratedItem gen = dbRepo.generateItem();

        if (gen == null || gen.item == null) {
            return msg.append("No item generated.");
        }
        Item item = gen.item;

        int newId = -1;
        if (gl.addItemCheck(item)) {
            if (item instanceof Consumable) {
                for (Item i : inv.getSlots()) {
                    if (i instanceof Consumable) {
                        Consumable existing = (Consumable) i;
                        if (existing.getName().equals(item.getName())) {
                            if (existing.getConsumableCount() < inv.getMaxStack()) {
                                try {
                                    int hasId = dbRepo.insertConsumable(1, gen.templateId);
                                    if (hasId > 0) {
                                        existing.setDbId(hasId);
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                                existing.increaseQuantity(1);
                                inv.setTotalWeight(gl.calculateTotalWeight());
                                return msg.append(existing.getName() + " stacked to " + existing.getConsumableCount());
                            }
                        }
                    }
                }
                if (!gl.addItemCheck(item)) {
                    msg.append(item);
                    msg.append("\nNot enough Room or carry capacity to add this item.\n");
                    return msg;
                }
                newId = dbRepo.insertConsumable(1, gen.templateId);
                if (newId > 0) {
                    item.setDbId(newId);
                }
                try {
                    msg.append(gl.addItem(item));
                }catch (NotEnoughInventorySpaceException e){
                    msg.append("Could not add ").append(item.getName()).append(": not enough inventory space.\n");
                } catch (TooMuchWeightException e) {
                    msg.append("Could not add ").append(item.getName()).append(": carrying too much weight already.\n");
                }
                return msg;
            }
        }


            // checker hvis item er conumable og allerade er der, så stacker den
            if (!gl.addItemCheck(item)) {
                msg.append(item);
                msg.append("Not enough room or carry capacity to add item");
                return msg;
            }
        if (gen.category.equals("weapon") && (item instanceof Weapon)) {
            newId = dbRepo.insertWeapon(1, gen.templateId);
        } else if (gen.category.equals("armor") && (item instanceof Armor)) {
            newId = dbRepo.insertArmor(1, gen.templateId);
        }

        if (newId > 0) {
            item.setDbId(newId);
        }
        try {
            msg.append(gl.addItem(item));
        }catch (NotEnoughInventorySpaceException e){
            msg.append("Could not add ").append(item.getName()).append(": not enough inventory space.\n");
        } catch (TooMuchWeightException e) {
            msg.append("Could not add ").append(item.getName()).append(": carrying too much weight already.\n");
        }
        return msg;
    }
    public int generateCoins(){
        int rand = (int)(Math.random() * 10) + 1;
        return rand;
    }
}
