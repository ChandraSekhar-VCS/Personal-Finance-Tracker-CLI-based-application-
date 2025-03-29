import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.util.stream.Collectors;

public class FinanceTracker {
    List<Transaction> transactions;
    List<Transaction> newTransactions;
    final String DATA_FILE = "transactions.csv";

    public FinanceTracker() {
        transactions = new ArrayList<>();
        newTransactions = new ArrayList<>();
        loadTransactions();
    }

    public void saveTransactions(){
        try (FileWriter writer = new FileWriter(DATA_FILE, true)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Transaction transaction : newTransactions) {
                String formattedTransaction = String.format(
                        "%s,%.2f,%s,%s,%s\n",
                        transaction.date().format(dateTimeFormatter),
                        transaction.amount(),
                        transaction.category().name(),
                        transaction.description(),
                        transaction.type().name()
                );
                writer.write(formattedTransaction);
            }
            newTransactions.clear();
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    public void addTransaction(){
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = null;
        double amount = 0;
        TransactionCategory category = null;
        String description = null;
        TransactionType type = null;

        // Input and validate date
        while (date == null) {
            System.out.print("Enter the transaction Date (yyyy-MM-dd): ");
            String dateString = scanner.nextLine();
            try {
                date = LocalDate.parse(dateString, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        // Input and validate amount
        while (true) {
            System.out.print("Enter the transaction Amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Amount must be positive.");
                } else {
                    break; // Valid amount, exit loop
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please enter a number.");
            }
        }

        // Input and validate category
        while (category == null) {
            System.out.print("Enter the transaction Category (GROCERIES, RENT, SALARY, ENTERTAINMENT, UTILITIES, TRANSPORTATION, OTHER): ");
            String categoryString = scanner.nextLine().toUpperCase();
            try {
                category = TransactionCategory.valueOf(categoryString);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid category.");
            }
        }

        // Input and validate description
        System.out.print("Enter the transaction Description: ");
        description = scanner.nextLine();

        // Input and validate type
        while (type == null) {
            System.out.print("Enter the transaction Type (INCOME/EXPENSE): ");
            String typeString = scanner.nextLine().toUpperCase();
            try {
                type = TransactionType.valueOf(typeString);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid transaction type.");
            }
        }

        Transaction transaction = new Transaction(date, amount, category, description, type);
        transactions.add(transaction);
        newTransactions.add(transaction);
        saveTransactions();
        System.out.println("Transaction saved successfully");
    }

    public void viewTransactions(LocalDate startDate, LocalDate endDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.printf("%-12s %-10s %-15s %-30s %-10s%n", "Date", "Amount", "Category", "Description", "Type");
        System.out.println("___________________________________________________________________________________");

        transactions.stream()
                .filter(transaction -> startDate == null || !transaction.date().isBefore(startDate))
                .filter(transaction -> endDate == null || !transaction.date().isAfter(endDate))
                .forEach(transaction -> {
                    System.out.printf(
                            "%-12s %-10.2f %-15s %-30s %-10s%n",
                            transaction.date().format(dateTimeFormatter),
                            transaction.amount(),
                            transaction.category().name(),
                            transaction.description(),
                            transaction.type().name()
                    );
                });
    }

    public void generateSummary(LocalDate startDate, LocalDate endDate,TransactionCategory category){
        double totalIncome = transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.INCOME)
                .filter(transaction -> startDate == null || !transaction.date().isBefore(startDate))
                .filter(transaction -> endDate == null || !transaction.date().isAfter(endDate))
                .filter(transaction -> category == null || transaction.category() == category) // Filter by category
                .mapToDouble(Transaction::amount)
                .sum();

        double totalExpenses = transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .filter(transaction -> startDate == null || !transaction.date().isBefore(startDate))
                .filter(transaction -> endDate == null || !transaction.date().isAfter(endDate))
                .filter(transaction -> category == null || transaction.category() == category) // Filter by category
                .mapToDouble(Transaction::amount)
                .sum();

        double netBalance = totalIncome - totalExpenses;

        System.out.println("\n--- Summary ---");
        if (startDate != null && endDate != null) {
            System.out.println("Date Range: " + startDate + " to " + endDate);
        }
        if(category != null){
            System.out.println("Category: " + category.name());
        }
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpenses);
        System.out.println("Net Balance: " + netBalance);
    }

    public void loadTransactions(){
        transactions.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    try {
                        LocalDate date = LocalDate.parse(parts[0], dateFormatter);
                        double amount = Double.parseDouble(parts[1]);
                        TransactionCategory category = TransactionCategory.valueOf(parts[2].toUpperCase());
                        String description = parts[3];
                        TransactionType type = TransactionType.valueOf(parts[4].toUpperCase());
                        Transaction transaction = new Transaction(date, amount, category, description, type);
                        transactions.add(transaction);
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date on line " + lineNumber + ": " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing amount on line " + lineNumber + ": " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error parsing category/type on line " + lineNumber + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid CSV format on line " + lineNumber + ": " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Transactions file not found.");
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void calculateBalance() {
        try {
            double totalIncome = transactions.stream()
                    .filter(transaction -> transaction.type() == TransactionType.INCOME)
                    .mapToDouble(Transaction::amount)
                    .sum();

            double totalExpenses = transactions.stream()
                    .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                    .mapToDouble(Transaction::amount)
                    .sum();

            double balance = totalIncome - totalExpenses;

            System.out.println("\n--- Current Balance ---");
            System.out.println("Balance: " + balance);
        } catch (NullPointerException e) {
            System.out.println("Error: Transaction list is empty or null.");
        } catch (Exception e) {
            System.out.println("Error calculating balance: " + e.getMessage());
        }
    }

    public void generateCategoryReports(){
        try {
            Map<TransactionCategory, Double> categoryExpenses = transactions.stream()
                    .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                    .collect(Collectors.groupingBy(Transaction::category, Collectors.summingDouble(Transaction::amount)));

            System.out.println("\n--- Category-wise Expenses ---");
            categoryExpenses.forEach((category, total) -> {
                System.out.println(category + ": " + total);
            });
        } catch (NullPointerException e) {
            System.out.println("Error: Transaction list is empty or null.");
        } catch (Exception e) {
            System.out.println("Error generating category reports: " + e.getMessage());
        }
    }
}
