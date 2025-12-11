package Logic.Inventory;

import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;
import Logic.DBRepo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Inventory {
    Random rand = new Random();
    private final int maxWeight = 50;
    private static final int maxStack = 5;
    private int coins;
    private final int maxSlots = 192;
    private int unlockedSlots = 32;
    private double totalWeight;
    private DBRepo dbRepo;

    private final List<Item> slots;


    public Inventory(int coins, int maxPotionStack, int maxSlots, int unlockedSlots, int maxWeight, int maxStack, double totalWeight) {
        this.coins = coins;
        this.unlockedSlots = unlockedSlots;
        this.totalWeight = totalWeight;
        this.slots = new ArrayList<>();
    }

    public double getTotalWeight(){
        return totalWeight;
    }

    public double calculateTotalWeight() {
        double sum = 0;
        for (Item item : slots) {
            sum += item.getWeight();
        }
        return sum;
    }
    public int getCoins() {
        return coins;
    }
    public static int getMaxStack() {
        return maxStack;
    }
    public int getMaxSlots() {
        return maxSlots;
    }
    public int getUnlockedSlots() {
        return unlockedSlots;
    }
    public List<Item> getSlots() {
        return slots;
    }

    public void setCoins (int coins) {
        this.coins = coins;
    }
    public void setUnlockedSlots(int unlockedSlots) {
        this.unlockedSlots = unlockedSlots;
    }
    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public boolean addItemCheck(Item item) {
            if(slots.size() >= unlockedSlots){
            return false;
    }
        if(getTotalWeight() + item.getWeight() > maxWeight) {
            return false;
        }
        return true;
    }
    public String addItem(Item item) {
       String msg = " ";

       //Hvis det er en Consumable så vil den kunne stacke med dette
       if (item instanceof Consumable) {
           Consumable newConsumable = (Consumable) item;

           for (Item i : slots){
               if (i instanceof Consumable){
                   Consumable existingConsumable = (Consumable) i;

                   if (existingConsumable.getName().equals(newConsumable.getName())){
                       if(existingConsumable.getConsumableCount() < getMaxStack()) {
                           existingConsumable.increaseQuantity(existingConsumable.getConsumableCount());
                           msg = existingConsumable.getName() + " stacked to " + existingConsumable.getConsumableCount();
                           return msg;
                       }
                   }
               }
           }
       }
       if(addItemCheck(item)){
        slots.add(item);
        setTotalWeight((int) calculateTotalWeight());
        msg = item + " added to inventory ";
       }
       else {
           msg = " not enough room or carry capacity to add item ";
       }
       return msg;
    }

    public String showInventory(){
        System.out.println("\n------Inventory------");
        System.out.println("Coins: " + coins + "\nTotal Weight: " + totalWeight + "\nUnlocked Slots: " + unlockedSlots + "\n");

        for (Item item : slots){
            if (item instanceof Weapon w){
                System.out.printf(" %d | %s | %s | %s | %.1f | %d | %d%n",w.getDbId(), w.getName() , w.getType(), w.getRarity(), w.getWeight(), w.getValue(), w.getDamage());
            } else if (item instanceof Armor a){
                System.out.printf(" %d | %s | %s | %.1f | %d | %d%n",a.getDbId(), a.getName(), a.getRarity(), a.getWeight(), a.getValue(), a.getDurability());
            } else if (item instanceof Consumable c){
                System.out.printf("%d | %s | %.1f | %d | %s | %d/%d%n",c.getDbId(), c.getName(), c.getWeight(), c.getValue(), c.getDescription(), c.getConsumableCount(), getMaxStack());
            }
        }
        if (slots.isEmpty()){
            return "Inventory is empty";
        }
        return slots.toString();
    }

    public void clearItems(){
        slots.clear();
    }

    public Item removeItemByDbId(int dbId) {
        Iterator<Item> it = slots.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getDbId() == dbId) {
                it.remove();
                setTotalWeight((int) calculateTotalWeight());
                return item;
            }
        }
        return null;
    }


}
