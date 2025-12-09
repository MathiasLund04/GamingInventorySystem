package CLI;

import Adventurer.Adventurer;
import Items.Item;
import Logic.GameLogic;
import Logic.DBConnection;
import Logic.DBRepo;
import Logic.Inventory.Inventory;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
        static Scanner scanner = new Scanner(System.in);
    public static void Menu() throws Exception {
        int choice;
        DBConnection db = new DBConnection();
        DBRepo dbRepo = new DBRepo(db);
        Inventory inv = new Inventory(0,5,192,32,50,20,0);
        Adventurer adventurer = new Adventurer();

        do {
            inv = dbRepo.loadInventory(inv);
            showMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 1 -> System.out.println(adventurer.goOnAdventure());
                case 2 -> {
                 if(inv !=null ){
                     dbRepo.loadInventory(inv);
                     inv.showInventory();
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
        } while (choice != 4);
        scanner.close();

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
                System.out.print("Invalid selection. Please enter a number between 1-4: ");
                scanner.next();
            }
        }
        return Choice;
    }
}
