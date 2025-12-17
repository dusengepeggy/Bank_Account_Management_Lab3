package utils;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.function.Predicate;

public class ValidationUtils {
    private static Scanner sc;
    
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^ACC\\d{3}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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

    public String readAccountNumber(String prompt) {
        return readValidatedString(prompt, this::isValidAccountNumber, 
            "Invalid account number format! Account number must match pattern: ACC### (e.g., ACC001)");
    }

    public String readEmail(String prompt) {
        return readValidatedString(prompt, this::isValidEmail, 
            "Invalid email format! Please enter a valid email address (e.g., user@example.com)");
    }

    private String readValidatedString(String prompt, Predicate<String> validator, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (validator.test(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    private boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            return false;
        }
        Matcher matcher = ACCOUNT_NUMBER_PATTERN.matcher(accountNumber);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
