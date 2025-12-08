package Logic.Inventory;

import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inventory {
    Random rand = new Random();
    private final int maxWeight = 50;
    private final int maxStack = 32;
    private int coins;
    private final int maxPotionStack = 16;
    private final int maxSlots = 192;
    private int unlockedSlots = 32;
    private int totalWeight;

    private final List<Item> slots = new ArrayList<>();


    public Inventory(int coins, int maxPotionStack, int maxSlots, int unlockedSlots, int maxWeight, int maxStack, int totalWeight) {
        this.coins = coins;
    }

    public void showInventory() {
        System.out.println("------Inventory------");
        System.out.println("Coins: " + coins + "\nTotal Weight: " + totalWeight + "\nUnlocked Slots: " + unlockedSlots + "\n");

        for (Item item : slots) {
            if (item instanceof Weapon w){
                System.out.println("Name     | WeaponType | Rarity | Weight | Value | Damage");
                System.out.printf("%s | %s | %s | %.1f | %d | %d%n ", w.getName() , w.getType(), w.getRarity(), w.getWeight(), w.getValue(), w.getDamage());
            } else if (item instanceof Armor a){
                System.out.println("Name    | Rarity | Weight | Value | Durability");
                System.out.printf("%s | %s | %.1f | %d | %d%n ", a.getName(), a.getRarity(), a.getWeight(), a.getValue(), a.getDurability());
            } else if (item instanceof Consumable c){
                System.out.println("Name    | Weight | Value | Description");
                System.out.printf("%s | %.1f | %d | %s%n ", c.getName(), c.getWeight(), c.getValue(), c.getDescription());
            }
        }
    }
    public double getTotalWeight() {
        double sum = 0;
        for (Item item : slots) {
            sum += item.getWeight();
        }
        return sum;
    }
    public int getCoins() {
        return coins;
    }
    public int getMaxStack() {
        return maxStack;
    }
    public int getMaxPotionStack() {
        return maxPotionStack;
    }
    public int getMaxSlots() {
        return maxSlots;
    }
    public int getUnlockedSlots() {
        return unlockedSlots;
    }

    public void setCoins (int coins) {
        this.coins = coins;
    }
    public void setUnlockedSlots(int unlockedSlots) {
        this.unlockedSlots = unlockedSlots;
    }

    public boolean addItemCheck(Item item) {
            if(slots.size() >=unlockedSlots){
            return false;

    }
        if(getTotalWeight() + item.getWeight() > maxWeight) {
            return false;
        }
        slots.add(item);
        return true;
    }


}
