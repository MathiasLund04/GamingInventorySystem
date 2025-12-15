package CLI;

import Adventurer.Adventurer;
import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;
import Logic.DBConnection;
import Logic.DBRepo;
import Logic.Inventory.Inventory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
        static Scanner input = new Scanner(System.in);
    public static void startMenu() throws Exception {
        int choice;
        DBConnection db = new DBConnection();
        DBRepo dbRepo = new DBRepo(db);
        Inventory inv = new Inventory(0, 5, 192, 32, 50, 20, 0);
        Adventurer adventurer = new Adventurer(inv, dbRepo);

        do {
            inv = dbRepo.loadInventory(inv);
            showMenu();
            choice = getChoice(input);

            switch (choice) {
                case 1 -> System.out.println(adventurer.goOnAdventure());
                case 2 -> {
                    if (inv != null) {
                        dbRepo.loadInventory(inv);
                        inv.showInventory();
                        while (choice != 6){
                            inventoryMenu();
                            choice = getChoice(input);
                            inventoryMenuChoices(inv, choice);

                        }
                    } else {
                        System.out.println("no inventory loaded");
                    }

                }
                case 3 -> dbRepo.testConnection();
                case 4 -> {
                    try {
                        dbRepo.loadInventory(inv);
                        System.out.println("Current inventory:");
                        inv.showInventory();
                        System.out.print("Enter the Id of the item to delete: ");
                        int deleteID = input.nextInt();
                        input.nextLine();
                        Item found = null;
                        for (Item it : inv.getSlots()) {
                            if (it.getDbId() == deleteID) {
                                found = it;
                                break;
                            }
                        }
                        if (found == null) {
                            System.out.println("Item with Id " + deleteID + " not found in inventory.");
                            break;
                        }

                        if (found instanceof Consumable) {
                            Consumable c = (Consumable) found;
                            if (c.getConsumableCount() > 1) {
                                boolean dbDecremented = false;
                                try {
                                    dbDecremented = dbRepo.deleteConsumable(c.getDbId());
                                } catch (Exception e) {
                                    System.out.println("Error decrementing consumable in DB: " + e.getMessage());
                                }
                                if (dbDecremented) {
                                    c.setConsumableCount(c.getConsumableCount() - 1);
                                    inv.setTotalWeight(inv.calculateTotalWeight());
                                    System.out.println("Decremented consumable '" + c.getName() + "'. New count: " + c.getConsumableCount());
                                } else {
                                    System.out.println("Failed to decrement consumable in database.");
                                }
                                break;
                            }
                        }
                        boolean dbDeleted = false;
                        try {
                            if (found instanceof Weapon) {
                                dbDeleted = dbRepo.deleteWeapon(deleteID);
                            } else if (found instanceof Armor) {
                                dbDeleted = dbRepo.deleteArmor(deleteID);
                            } else if (found instanceof Consumable) {
                                dbDeleted = dbRepo.deleteConsumable(deleteID);
                            }
                        } catch (Exception e) {
                            System.out.println("Error deleting item from database: " + e.getMessage());
                        }

                        if (dbDeleted) {
                            for (Item it : inv.getSlots()) {
                                if (it.getDbId() == deleteID) {
                                    int addingCoins = it.getValue();
                                    inv.setCoins(inv.getCoins() + addingCoins);
                                    dbRepo.updateCoins(inv.getCoins());
                                }
                            }
                            inv.removeItemByDbId(deleteID);
                            System.out.println("Item removed successfully.");
                        } else {
                            System.out.println("Failed to remove item from database. Item not removed.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error while deleting item: " + e.getMessage());
                        input.nextLine();

                    }
                }
                case 5 -> {
                    while(inv.addSlots()){
                        System.out.println("want to buy more space (+ 32 slots)\n" +
                                " Yes(Y) or No(N)");
                        String choice2 = input.nextLine();
                        if (choice2.equals("Yes") || choice2.equals("Y")) {
                            inv.setUnlockedSlots(inv.getUnlockedSlots() + 32);
                        } else if (choice2.equals("No") || choice2.equals("N")) {
                            break;
                        }
                    }
                }
                case 6 -> {
                    System.out.println("Thank you for playing!");
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice.");


            }
        }
            while (choice != 5) ;
            input.close();

    }

    public static void showMenu() {
        System.out.println("Legend of CodeCraft");
        System.out.println("1. Go on an adventure");
        System.out.println("2. Look at your inventory");
        System.out.println("3. Test Connection");
        System.out.println("4. sell an item");
        System.out.println("5. Close the game");
    }

    public static int getChoice (Scanner scanner) {
        int Choice = -1;
        while (true) {
            try {
                Choice = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Invalid selection. Please enter a number between 1-5: ");
                scanner.next();
            }
        }
        return Choice;
    }

    public static void inventoryMenu(){
        System.out.println("1. Show inventory (Not sorted)");
        System.out.println("2. Show inventory (Sorted by choice)");
        System.out.println("3. Sell item");
        System.out.println("4. Buy slots");
        System.out.println("5. Back to menu");
    }

    public static void inventoryMenuChoices(Inventory inv, int choice){
        switch (choice){
            case 1 -> inv.showInventory();
            case 2 -> {
                System.out.println("NOT WORKING RIGHT NOW");
                showChoices();
                sortingChoice(inv, choice);
            }
            case 3 -> System.out.println("GET DELETE IN A METHOD");
            case 4 -> System.out.println("BUY SLOTS (ALSO IN A METHOD)");
            case 5 -> {
                System.out.println("Going back to menu");
                choice = 6;
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    public static void sortingChoice(Inventory inv, int choice){
        switch (choice){
            case 1 -> inv.showInventoryById();
            case 2 -> inv.showInventory();
            case 3 -> inv.showInventory();
            case 4 -> inv.showInventory();
            case 5 -> {
                System.out.println("Going back to inventory menu");
                choice = 6; //Working on it
            }
        }
    }

    public static void showChoices(){
        System.out.println("1. Order by time added");
        System.out.println("2. Order by item type");
        System.out.println("3. Order by value");
        System.out.println("4. Order by weight");
        System.out.println("5. Back to inventory menu");
    }
}
