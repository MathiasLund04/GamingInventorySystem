package Inventory;

import Item.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final int maxWeight = 50;
    private final int maxStack = 32;
    private int coins;
    private final int maxPotionStack = 16;
    private final int maxSlots = 192;
    private int unlockedSlots = 32;

    private final List<Item> Slots = new ArrayList<>();


    public Inventory(int coins, int maxPotionStack, int maxSlots, int unlockedSlots, int maxWeight, int maxStack) {
        this.coins = coins;
    }

    public void showInventory() {

    }

    public double getTotalWeight() {
        double sum = 0;
        for (Item item : Slots) {
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
        if (Slots.size() >= unlockedSlots) {
            return false;
        }
        if(getTotalWeight() + item.getWeight() > maxWeight) {
            return false;
        }
        Slots.add(item);
        return true;
    }
}
