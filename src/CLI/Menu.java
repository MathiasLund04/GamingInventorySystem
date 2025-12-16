package CLI;

import Adventurer.Adventurer;
import Exceptions.NotEnoughCoinsException;
import Items.Armor;
import Items.Consumable;
import Items.Item;
import Items.Weapon;
import Logic.DBConnection;
import Logic.DBRepo;
import Logic.GameLogic;
import Logic.Inventory.Inventory;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
        private static Scanner input = new Scanner(System.in);
        private static DBConnection db = new DBConnection();
        private static DBRepo dbRepo = new DBRepo(db);
        private static Inventory inv = new Inventory(0,  32, 0);
        private static Adventurer adventurer = new Adventurer(inv, dbRepo);
        private static GameLogic gl = new GameLogic(inv);
    public static void startMenu() throws Exception {
        int choice;

        do {
            inv = dbRepo.loadInventory(inv,gl);
            showMenu();
            choice = getChoice(input);
            input.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println(adventurer.goOnAdventure());
                    continueAdventure();
                }
                case 2 -> {
                    if (inv != null) {
                        while (choice != 5){
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
                    System.out.println("Thank you for playing!");
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice.");


            }
        }
            while (choice != 4) ;
            input.close();

    }

    public static void showMenu() {
        System.out.println("Legend of CodeCraft");
        System.out.println("1. Go on an adventure");
        System.out.println("2. Look at your inventory");
        System.out.println("3. Test Connection");
        System.out.println("4. Close the game");

    }

    public static int getChoice (Scanner scanner) {
        int Choice = -1;
        while (true) {
            try {
                Choice = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Invalid selection. Please enter a number between 1-7: ");
                scanner.next();
            }
        }
        return Choice;
    }

    public static void continueAdventure() throws Exception{
        boolean going = true;
        while (going) {
            System.out.println("Do you want to continue?");
            System.out.println("Yes(Y) or No(N)");
            String adventureChoice = input.nextLine();
            if (adventureChoice.equalsIgnoreCase("Yes") || adventureChoice.equalsIgnoreCase("Y")) {
                System.out.println(adventurer.goOnAdventure());
            } else if (adventureChoice.equalsIgnoreCase("No") || adventureChoice.equalsIgnoreCase("N")) {
                going = false;
            }
        }
    }

    public static void inventoryMenu(){
        System.out.println("1. Show inventory (Not sorted)");
        System.out.println("2. Show inventory (Sorted by choice)");
        System.out.println("3. Sell item");
        System.out.println("4. Buy slots");
        System.out.println("5. Back to menu");
    }

    public static void inventoryMenuChoices(Inventory inv, int choice) throws Exception {
        dbRepo.loadInventory(inv,gl);
        switch (choice){
            case 1 -> System.out.println(gl.showInventory());
            case 2 -> {
                while (choice != 7) {
                    showChoices();
                    choice = getChoice(input);
                    sortingChoice(gl, choice);
                }
            }
            case 3 -> {
                try {
                    dbRepo.loadInventory(inv,gl);
                    System.out.println("Current inventory:");
                    gl.showInventory();
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
                                inv.setTotalWeight(gl.calculateTotalWeight());
                                System.out.println("Decremented consumable '" + c.getName() + "'. New count: " + c.getConsumableCount());
                            } else {
                                System.out.println("Failed to decrement consumable in database.");
                            }
                            break;
                        }
                    }
                    boolean dbDeleted = false;
                    try {
                        switch (found) {
                            case Weapon weapon -> dbDeleted = dbRepo.deleteWeapon(deleteID);
                            case Armor armor -> dbDeleted = dbRepo.deleteArmor(deleteID);
                            case Consumable consumable -> dbDeleted = dbRepo.deleteConsumable(deleteID);
                            default -> {
                            }
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
                        gl.removeItemByDbId(deleteID);
                        System.out.println("Item removed successfully.");
                    } else {
                        System.out.println("Failed to remove item from database. Item not removed.");
                    }
                } catch (Exception e) {
                    System.out.println("Error while deleting item: " + e.getMessage());
                    input.nextLine();

                }
            }

            case 4 -> {
                        int price = 300;
                    try {
                        coinsCheck(inv.getCoins(), price);
                        while(gl.addSlotsCheck()){
                            input.nextLine();
                            System.out.println("want to buy more space (+ 32 slots) for 300 coins");
                            System.out.println(" Yes(Y) or No(N)");
                            String choice2 = input.nextLine();
                            if (choice2.equalsIgnoreCase("Yes") || choice2.equalsIgnoreCase("Y")) {
                                dbRepo.updateUnlockedSlots(inv.getUnlockedSlots() + 32);
                                dbRepo.updateCoins(inv.getCoins() - price);
                                break;
                            } else if (choice2.equalsIgnoreCase("No") || choice2.equalsIgnoreCase("N")) {
                            break;
                            }
                        }
                    } catch (NotEnoughCoinsException e) {
                        System.out.println(e.getMessage());
                    }

            }
            case 5 -> {
                System.out.println("Going back to menu\n");
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    public static void sortingChoice(GameLogic gl, int choice){
        switch (choice){
            case 1 -> {
                Menu.gl.bubbleSortById();
                System.out.println(gl.showInventory());
            }
            case 2 -> {
                gl.bubbleSortByNewest();
                System.out.println(gl.showInventory());
            }
            case 3 -> {
                priorities();
                int typeChoice = getChoice(input);
                gl.bubbleSortByType(typeChoice);
                System.out.println(gl.showInventory());
            }
            case 4 -> {
                gl.bubbleSortByValue();
                System.out.println(gl.showInventory());
            }
            case 5 -> {
                gl.bubbleSortByWeight();
                System.out.println(gl.showInventory());
            }
            case 6 -> {
                itemType();
                int choice5 = getChoice(input);
                itemChoice(choice5);
            }
            case 7 -> {
                System.out.println("Going back to inventory menu\n");
            }
        }
    }

    public static void showChoices(){
        System.out.println("1. Order by time added (Oldest first)");
        System.out.println("2. Order by time added (Newest first)");
        System.out.println("3. Order by item type");
        System.out.println("4. Order by value");
        System.out.println("5. Order by weight");
        System.out.println("6. Select item type");
        System.out.println("7. Back to inventory menu");
    }

    public static void priorities(){
        System.out.println("1. Weapon first");
        System.out.println("2. Armor first");
        System.out.println("3. Consumable first");
        System.out.print("Your choice: ");
    }

    public static void itemType(){
        System.out.println("1. Weapons");
        System.out.println("2. Armor");
        System.out.println("3. Consumables");
        System.out.print("Your choice: ");
    }

    public static void itemChoice(int choice){
        switch (choice){
            case 1 -> System.out.println(gl.showWeapons());
            case 2 -> System.out.println(gl.showArmor());
            case 3 -> System.out.println(gl.showConsumable());
        }
    }

    public static void coinsCheck(int amount,int price) throws NotEnoughCoinsException {
        if(amount < price) {
            throw new NotEnoughCoinsException("Not enough coins.");
        } else{
            System.out.println("You have enough coins.\n");
        }

    }
}
