package Logic.Inventory;

import Exceptions.NotEnoughInventorySpaceException;
import Exceptions.TooMuchWeightException;
import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;
import Logic.DBRepo;

import java.util.*;

public class Inventory {
    Random rand = new Random();
    private final int maxWeight = 50;
    private static final int maxStack = 5;
    private int coins;
    private final int maxSlots = 192;
    private int unlockedSlots = 10;
    private double totalWeight;
    private DBRepo dbRepo;

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

    public double calculateTotalWeight() {
        double sum = 0;
        for (Item item : slots) {
            if (item instanceof Consumable) {
                sum += (item.getWeight()*((Consumable) item).getConsumableCount());
            } else {
                sum += item.getWeight();
            }
        }
        //For at give tallet en decimal så det ser mere overskueligt ud
        double rounded = Math.round(sum*10.0)/10.0;

        return rounded;
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
    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public boolean addItemCheck(Item item) {
            if (item instanceof Consumable) {
                Consumable newConsumable = (Consumable) item;
                for (Item i : slots) {
                    if (i instanceof Consumable) {
                        Consumable existingConsumable = (Consumable) i;
                        if (existingConsumable.getName().equals(newConsumable.getName())) {
                            if (existingConsumable.getConsumableCount() < 5) {
                                return true;
                            }
                        }
                    }
                }
            }
        if (slots.size() >= unlockedSlots) {
            return false;
        } else if (getTotalWeight() + item.getWeight() > maxWeight) {
            return false;
        } else {
            return true;
        }

    }
    public String addItem(Item item) throws NotEnoughInventorySpaceException, TooMuchWeightException {
       String msg = " ";

       //Hvis det er en Consumable så vil den kunne stacke med dette
       if (item instanceof Consumable) {
           Consumable newConsumable = (Consumable) item;

           for (Item i : slots){
               if (i instanceof Consumable){
                   Consumable existingConsumable = (Consumable) i;

                   if (existingConsumable.getName().equals(newConsumable.getName())){
                       if(existingConsumable.getConsumableCount() < getMaxStack()) {
                           existingConsumable.increaseQuantity(1);
                           setTotalWeight(calculateTotalWeight());
                           msg = existingConsumable.getName() + " stacked to " + existingConsumable.getConsumableCount();
                           return msg;
                       }
                   }
               }
           }
       }
       if(addItemCheck(item)){
        slots.add(item);
        setTotalWeight(calculateTotalWeight());
        msg = item.toString() + " added to inventory ";
       }
       else if (slots.size() >= unlockedSlots){
           throw new NotEnoughInventorySpaceException("Not enough inventory slots to add the item: " + item.getName());
       } else if (getTotalWeight() + item.getWeight() > maxWeight) {
           throw new TooMuchWeightException("Adding " + item.getName() + " would exceed carry capacity.");
       }
       return msg;
    }

    public StringBuilder showInventory(){
        //For at kunne opdatere total weight så den passer til inventory
        setTotalWeight(calculateTotalWeight());

        StringBuilder msg = new StringBuilder("\n------Inventory------");
        msg.append("\nCoins: " + getCoins() + "\nTotal Weight: " + getTotalWeight() + "\nUnlocked Slots: " + getUnlockedSlots() + "/" + getMaxSlots() + "\n");

        for (Item item : slots){
            msg.append("------\n");
            msg.append(item).append("\n");

            if (slots.isEmpty()){
                return msg.append("Inventory is empty");
            }
        }
        msg.append("------------------------");
        return msg;
    }

    public StringBuilder showWeapons(){
        StringBuilder msg = new StringBuilder("\n------Weapons------");
        for (Item item : slots){
            if (item instanceof Weapon){
                msg.append("------\n");
                msg.append(item).append("\n");
            }
        }
        return msg;
    }

    public StringBuilder showArmor(){
        StringBuilder msg = new StringBuilder("\n------Armor------");
        for (Item item : slots){
            if (item instanceof Armor){
                msg.append("------\n");
                msg.append(item).append("\n");
            }
        }
        return msg;
    }

    public StringBuilder showConsumable(){
        StringBuilder msg = new StringBuilder("\n------Consumables------");
        for (Item item : slots){
            if (item instanceof Consumable){
                msg.append("------\n");
                msg.append(item).append("\n");
            }
        }
        return msg;
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
                setTotalWeight(calculateTotalWeight());
                return item;
            }
        }
        return null;
    }

//Sortering
    public void bubbleSortById(){
        int size = slots.size();
        for (int i = 0; i < size; i++){
            boolean swapped = false;

            for (int j = 0; j < size - 1; j++){
                if (slots.get(j).getDbId() > slots.get(j+1 ).getDbId()){
                    Item temp = slots.get(j);
                    slots.set(j, slots.get(j+1));
                    slots.set(j + 1, temp);

                    swapped = true;
                }
            }
            if (!swapped){
                break;
            }
        }
    }

    public void bubbleSortByNewest(){
        int size = slots.size();
        for (int i = 0; i < size; i++){
            boolean swapped = false;
            for (int j = 0; j < size - 1; j++){
                if (slots.get(j).getDbId() < slots.get(j+1).getDbId()){
                    Item temp = slots.get(j);
                    slots.set(j, slots.get(j+1));
                    slots.set(j + 1, temp);

                    swapped = true;
                }
            }
            if (!swapped){
                break;
            }
        }
    }

    public void bubbleSortByValue(){
        int size = slots.size();
        for (int i = 0; i < size; i++){
            boolean swapped = false;

            for (int j = 0; j < size - 1; j++){
                if (slots.get(j).getValue() > slots.get(j+1).getValue()){
                    Item temp = slots.get(j);
                    slots.set(j, slots.get(j+1));
                    slots.set(j + 1, temp);
                    
                    swapped = true;
                }
            }
            if (!swapped){
                break;
            }
        }
    }

    public void bubbleSortByWeight(){
        int size = slots.size();
        for (int i = 0; i < size; i++){
            boolean swapped = false;
            for (int j = 0; j < size - 1; j++){
                if (slots.get(j).getWeight() > slots.get(j+1).getWeight()){
                    Item temp = slots.get(j);
                    slots.set(j, slots.get(j+1));
                    slots.set(j + 1, temp);

                    swapped = true;
                }
            }
            if (!swapped){
                break;
            }
        }
    }

    public void bubbleSortByType(int typePriority){
        int size = slots.size();
        for (int i = 0; i < size; i++){
            boolean swapped = false;
            for (int j = 0; j < size - 1; j++){
                int p1 = getTypePriority(slots.get(j), typePriority);
                int p2 = getTypePriority(slots.get(j+1), typePriority);

                if (p1 > p2){
                    Item temp = slots.get(j);
                    slots.set(j, slots.get(j+1));
                    slots.set(j + 1, temp);

                    swapped = true;
                }
            }
            if (!swapped){
                break;
            }
        }

    }


    public boolean addSlotsCheck(){
        if (coins>300 && unlockedSlots < maxSlots){
            return true;
        } else{
            return false;
        }
    }

    //Brugerdefineret prioritet ud fra hvad de vil have skal være først
    private int getTypePriority(Item item, int typePriority){

    if (typePriority == 1 && item instanceof Weapon)return 1;
    if (typePriority == 2 && item instanceof Armor) return 1;
    if (typePriority == 3 && item instanceof Consumable)return 1;

    if (item instanceof Weapon) return 2;
    if (item instanceof Armor) return 3;
    if (item instanceof Consumable) return 4;

    //Hvis ikke den er weapon, armor eller consumable, returneres dette (Kun hvis der er en eller anden form for gejl i den undtastede paraneter)
    return Integer.MAX_VALUE;

    }


}
