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

    public String goOnAdventure() throws Exception {
        Item generated = dbRepo.generateItem();
        String addedItem = inv.addItem(generated);
        return addedItem;
    }


}
