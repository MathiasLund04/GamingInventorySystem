package Logic;

import Items.Item;
import Logic.Inventory.Inventory;

import java.util.Random;

public class GameLogic {
 Random random = new Random();
 int rand = (int) (Math.random() * 5);
 private Inventory inventory;
 private DBRepo dbRepo;

 public GameLogic(Inventory inventory, DBRepo dbRepo) {
   this.inventory = inventory;
   this.dbRepo = dbRepo;
 }

}
