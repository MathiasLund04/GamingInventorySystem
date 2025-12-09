package Adventurer;

import Items.Item;
import Logic.DBConnection;
import Logic.DBRepo;
import Logic.GameLogic;
import Logic.Inventory.Inventory;

public class Adventurer {
    DBConnection db = new DBConnection();
    DBRepo dbRepo = new DBRepo(db);
    Inventory inv = new Inventory(0,16,192,32,50,32, 0);
    GameLogic gameLogic = new GameLogic(inv, dbRepo);

public Adventurer() {}

    public Adventurer(Inventory inventory, DBRepo dbRepo) {
        if (inventory != null) this.inv = inventory;
        if (dbRepo != null) this.dbRepo = dbRepo;
        this.gameLogic = new GameLogic(this.inv, this.dbRepo);
    }

    public String goOnAdventure() throws Exception {
        Item generated = dbRepo.generateItem();
        if (generated != null) {
            return "no item has been generated";
        }
        String addedItem = inv.addItem(generated);
        return addedItem;
    }


}
