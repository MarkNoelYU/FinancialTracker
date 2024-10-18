package com.pluralsight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        File file = new File(fileName);

        try {

            if (!file.exists()) {
                System.out.println("File does not exist. Creating a new file: " + fileName);
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;


            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");


                if (parts.length == 5) {
                    String date = parts[0];
                    String time = parts[1];
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount;


                    try {
                        amount = Double.parseDouble(parts[4]);
                        if (amount <= 0) {
                            System.out.println("Invalid amount in transaction, must be positive: " + line);
                            continue; // Skip if the amount is not positive
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount format: " + parts[4]);
                        continue; // Skip this line if the amount is invalid
                    }


                    Transaction transaction = new Transaction(date, time, description, vendor, amount);
                    transactions.add(transaction);
                } else {
                    System.out.println("Invalid transaction format: " + line);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading transactions from file: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {
        System.out.println("Enter the deposit date and time (yyyy-MM-dd HH:mm:ss): ");
        String dateTimeInput = scanner.nextLine();

        LocalDateTime dateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTime = LocalDateTime.parse(dateTimeInput, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date and time format. Please use yyyy-MM-dd HH:mm:ss.");
            return;
        }

        String date = dateTime.toLocalDate().toString();
        String time = dateTime.toLocalTime().toString();

        System.out.println("Enter the description of the deposit: ");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount (positive number): ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered. Please enter a valid number.");
            return;
        }


        Transaction newDeposit = new Transaction(date, time, description, vendor, amount);
        transactions.add(newDeposit);
        System.out.println("Deposit added successfully.");
    }


    public static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        System.out.println("Enter the payment date and time (yyyy-MM-dd HH:mm:ss): ");
        String dateTimeInput = scanner.nextLine();
        System.out.println("Enter the payment " +
                "+date and time (yyyy-MM-dd HH:mm:ss): ");
        (scanner) = scanner.nextLine();

        LocalDateTime dateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTime = (LocalDateTime);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date and time format. Please use yyyy-MM-dd HH:mm:ss.");
            return;
        }

        String date = dateTime.toLocalDate().toString();
        String time = dateTime.toLocalTime().toString();

        System.out.println("Enter the description of the payment: ");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount (positive number): ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered. Please enter a valid number.");
            return;
        }

        amount = -amount;
        Transaction newPayment = new Transaction( description, vendor, amount);
        transactions.add(newPayment);
        System.out.println("Payment added successfully.");
    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.printf("Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }
    System.out.printf("%-15s %-15s %-25s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
    System.out.println("-------------------------------------------------------------------------------");

    boolean hasDeposits = false;

    for (Transaction transaction : transactions) {
        if (transaction.getAmount() > 0) {
            System.out.printf("%-15s %-15s %-25s %-20s %-10.2f%n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
            hasDeposits = true;
        }
    }

    if (!hasDeposits) {
        System.out.println("No deposits found.");
    }
}

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {


            }
        }
    }


/*    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }*/



    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        boolean hasTransactions = false;

        System.out.printf("%-15s %-15s %-25s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate());

            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                    (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate))) {

                // Print the transaction details
                System.out.printf("%-15s %-15s %-25s %-20s %-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());

                hasTransactions = true;
            }
        }

        if (!hasTransactions) {
            System.out.println("No transactions found within the specified date range.");
        }
    }


/*    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }*/

    private static void filterTransactionsByVendor(String vendor) {
        boolean hasTransactions = false;

        System.out.printf("%-15s %-15s %-25s %-20s %-10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            // Check if the transaction's vendor matches the specified vendor name (case-insensitive)
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                // Print the matching transaction details
                System.out.printf("%-15s %-15s %-25s %-20s %-10.2f%n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());

                hasTransactions = true;
            }
        }


        if (!hasTransactions) {
            System.out.println("No transactions found for vendor: " + vendor);
        }
    }
}

