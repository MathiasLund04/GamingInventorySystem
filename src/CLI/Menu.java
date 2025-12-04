package CLI;

import Adventurer.Adventurer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
        static Scanner scanner = new Scanner(System.in);
    public static void Menu(String[] args) {
        int choice;


        do {
            showMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 1 -> Adventurer.goOnAdventure();
                    break;
                case 2 ->
                    break;
                case 3 ->
                    break;
                case 4 -> System.out.println("Thank you for playing!");
                        System.exit(0);
                default -> System.out.println("Invalid choice.");

            }
        } while (choice != 4);
        scanner.close();

    }

    public static void showMenu() {
        System.out.println("Legend of CodeCraft");
        System.out.println("1. Go on an adventure");
        System.out.println("2. Look at your inventory");
        System.out.println("3. Buy more inventory space");
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
