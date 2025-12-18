package Logic;

import Exceptions.NotEnoughInventorySpaceException;
import Exceptions.TooMuchWeightException;
import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;
import Logic.Inventory.Inventory;
import java.util.Iterator;

public class GameLogic {
 private Inventory inv;

 public GameLogic(Inventory inventory) {
  this.inv = inventory;
 }
 public double calculateTotalWeight() {
  double sum = 0;
  for (Item item : inv.getSlots()) {
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
 public boolean addItemCheck(Item item) {
  if (item instanceof Consumable) {
   Consumable newConsumable = (Consumable) item;
   for (Item i : inv.getSlots()) {
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
  if (inv.getSlots().size() >= inv.getUnlockedSlots()) {
   return false;
  } else if (inv.getTotalWeight() + item.getWeight() > inv.getMaxWeight()) {
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

   for (Item i : inv.getSlots()){
    if (i instanceof Consumable){
     Consumable existingConsumable = (Consumable) i;

     if (existingConsumable.getName().equals(newConsumable.getName())){
      if(existingConsumable.getConsumableCount() < inv.getMaxStack()) {
       existingConsumable.increaseQuantity(1);
       inv.setTotalWeight(calculateTotalWeight());
       msg = existingConsumable.getName() + " stacked to " + existingConsumable.getConsumableCount();
       return msg;
      }
     }
    }
   }
  }
  if(addItemCheck(item)){
   inv.getSlots().add(item);
   inv.setTotalWeight(calculateTotalWeight());
   msg = item.toString() + " added to inventory ";
  }
  else if (inv.getSlots().size() >= inv.getUnlockedSlots()){
   throw new NotEnoughInventorySpaceException("Not enough inventory slots to add the item: " + item.getName());
  } else if (inv.getTotalWeight() + item.getWeight() > inv.getMaxWeight()) {
   throw new TooMuchWeightException("Adding " + item.getName() + " would exceed carry capacity.");
  }
  return msg;
 }
 public StringBuilder showInventory(){
  //For at kunne opdatere total weight så den passer til inventory
  inv.setTotalWeight(calculateTotalWeight());

  StringBuilder msg = new StringBuilder("\n------Inventory------");
  msg.append("\nCoins: " + inv.getCoins() + "\nTotal Weight: " + inv.getTotalWeight() + "\nUnlocked Slots: " + inv.getUnlockedSlots() + "/" + inv.getMaxSlots() + "\n");

  for (Item item : inv.getSlots()){
   msg.append("------\n");
   msg.append(item);

   if (inv.getSlots().isEmpty()){
    return msg.append("Inventory is empty");
   }
  }
  msg.append("------------------------");
  return msg;
 }
 public StringBuilder showWeapons(){
  StringBuilder msg = new StringBuilder("\n------Weapons------");
  for (Item item : inv.getSlots()){
   if (item instanceof Weapon){
    msg.append("------\n");
    msg.append(item).append("\n");
   }
  }
  return msg;
 }
 public StringBuilder showArmor(){
  StringBuilder msg = new StringBuilder("\n------Armor------");
  for (Item item : inv.getSlots()){
   if (item instanceof Armor){
    msg.append("------\n");
    msg.append(item).append("\n");
   }
  }
  return msg;
 }
 public StringBuilder showConsumable(){
  StringBuilder msg = new StringBuilder("\n------Consumables------");
  for (Item item : inv.getSlots()){
   if (item instanceof Consumable){
    msg.append("------\n");
    msg.append(item).append("\n");
   }
  }
  return msg;
 }
 public void clearItems(){
  inv.getSlots().clear();
 }
 public Item removeItemByDbId(int dbId) {
  Iterator<Item> it = inv.getSlots().iterator();
  while (it.hasNext()) {
   Item item = it.next();
   if (item.getDbId() == dbId) {
    it.remove();
    inv.setTotalWeight(calculateTotalWeight());
    return item;
   }
  }
  return null;
 }
 //Sortering
 public void bubbleSortByOldest(){
  int size = inv.getSlots().size();
  for (int i = 0; i < size; i++){
   boolean swapped = false;
   
   for (int j = 0; j < size - i - 1; j++){
    if (inv.getSlots().get(j).getDbId() > inv.getSlots().get(j+1 ).getDbId()){
     Item temp = inv.getSlots().get(j);
     inv.getSlots().set(j, inv.getSlots().get(j+1));
     inv.getSlots().set(j + 1, temp);

     swapped = true;
    }
   }
   if (!swapped){
    break;
   }
  }
 }
 public void bubbleSortByNewest(){
  int size = inv.getSlots().size();
  for (int i = 0; i < size; i++){
   boolean swapped = false;
   for (int j = 0; j < size - i - 1; j++){
    if (inv.getSlots().get(j).getDbId() < inv.getSlots().get(j+1).getDbId()){
     Item temp = inv.getSlots().get(j);
     inv.getSlots().set(j, inv.getSlots().get(j+1));
     inv.getSlots().set(j + 1, temp);

     swapped = true;
    }
   }
   if (!swapped){
    break;
   }
  }
 }
 public void bubbleSortByValue(){
  int size = inv.getSlots().size();
  for (int i = 0; i < size; i++){
   boolean swapped = false;

   for (int j = 0; j < size - i - 1; j++){
    if (inv.getSlots().get(j).getValue() > inv.getSlots().get(j+1).getValue()){
     Item temp = inv.getSlots().get(j);
     inv.getSlots().set(j, inv.getSlots().get(j+1));
     inv.getSlots().set(j + 1, temp);

     swapped = true;
    }
   }
   if (!swapped){
    break;
   }
  }
 }
 public void bubbleSortByWeight(){
  int size = inv.getSlots().size();
  for (int i = 0; i < size; i++){
   boolean swapped = false;
   for (int j = 0; j < size - i - 1; j++){
    if (inv.getSlots().get(j).getWeight() < inv.getSlots().get(j+1).getWeight()){
     Item temp = inv.getSlots().get(j);
     inv.getSlots().set(j, inv.getSlots().get(j+1));
     inv.getSlots().set(j + 1, temp);

     swapped = true;
    }
   }
   if (!swapped){
    break;
   }
  }
 }
 public void bubbleSortByType(int typePriority){
  int size = inv.getSlots().size();
  for (int i = 0; i < size; i++){
   boolean swapped = false;
   for (int j = 0; j < size - i - 1; j++){
    int p1 = getTypePriority(inv.getSlots().get(j), typePriority);
    int p2 = getTypePriority(inv.getSlots().get(j+1), typePriority);

    if (p1 > p2){
     Item temp = inv.getSlots().get(j);
     inv.getSlots().set(j, inv.getSlots().get(j+1));
     inv.getSlots().set(j + 1, temp);

     swapped = true;
    }
   }
   if (!swapped){
    break;
   }
  }

 }
 public boolean addSlotsCheck(){
  if (inv.getCoins()>300 && inv.getUnlockedSlots() < inv.getMaxSlots()){
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
