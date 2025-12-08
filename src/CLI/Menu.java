package CLI;

import Adventurer.Adventurer;
import Logic.DBConnection;
import Logic.DBRepo;
import Logic.Inventory.Inventory;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
        static Scanner scanner = new Scanner(System.in);
    public static void Menu() throws Exception {
        int choice;
        DBConnection db = new DBConnection();
        DBRepo dbRepo = new DBRepo(db);
        Inventory inv = new Inventory(0,16,192,32,50,32, 0);


        do {
            dbRepo.loadInventory();
            showMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 1 -> dbRepo.generateItem();
                case 2 -> {
                    inv.showInventory();
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
