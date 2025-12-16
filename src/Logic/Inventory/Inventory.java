package Logic.Inventory;
import Items.Item;
import java.util.*;

public class Inventory {
    private final int maxWeight = 50;
    private static final int maxStack = 5;
    private int coins;
    private final int maxSlots = 192;
    private int unlockedSlots = 10;
    private double totalWeight;
    private final List<Item> slots;
    public Inventory(int coins, int unlockedSlots, double totalWeight) {
        this.coins = coins;
        this.unlockedSlots = unlockedSlots;
        this.totalWeight = totalWeight;
        this.slots = new ArrayList<>();
    }
    public double getTotalWeight(){
        return totalWeight;
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
    public int getMaxWeight() {
        return maxWeight;
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
    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }


}
