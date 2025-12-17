package utils;

import java.util.Scanner;

public class ValidationUtils {
    private static Scanner sc;

    public ValidationUtils(Scanner sc) {
        ValidationUtils.sc = sc;
    }

    public int readInt(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                value = sc.nextInt();
                sc.nextLine();
                if (value >= min && value <= max) return value;
                System.out.println("Input must be between " + min + " and " + max + "!");
            } else {
                System.out.println("Invalid input! Enter a number.");
                sc.nextLine();
            }
        }
    }

    public double readDouble(String prompt, double min) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                value = sc.nextDouble();
                sc.nextLine();
                if (value >= min) return value;
                System.out.println("Amount must be at least $" + min);
            } else {
                System.out.println("Invalid input! Enter a number.");
                sc.nextLine();
            }
        }
    }

    public String readNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty!");
        }
    }

}
