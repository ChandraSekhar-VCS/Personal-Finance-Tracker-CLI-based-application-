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
        try(FileWriter writer = new FileWriter(DATA_FILE,true)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for(Transaction transaction : newTransactions){
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
        }
        catch (IOException e){
            System.out.println("Error saving the transaction " +  e.getMessage());
        }
    }

    public void addTransaction(){
        try{
            Scanner  scanner = new Scanner(System.in);
            System.out.println("Enter the transaction Date(yyyy-MM-dd): ");
            String dateString = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("Enter the transaction Amount: ");
            double  amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter the transaction Category (GROCERIES,RENT,SALARY,ENTERTAINMENT,UTILITIES,TRANSPORTATION,OTHER): ");
            String categoryString = scanner.nextLine().toUpperCase();
            TransactionCategory category = TransactionCategory.valueOf(categoryString);
            System.out.println("Enter the transaction Description: ");
            String description = scanner.nextLine();
            System.out.println("Enter the transaction Type (INCOME/EXPENSE): ");
            String typeString = scanner.nextLine().toUpperCase();
            TransactionType type = TransactionType.valueOf(typeString);

            Transaction transaction = new Transaction(date,amount,category,description,type);
            transactions.add(transaction);
            newTransactions.add(transaction);
            saveTransactions();
            System.out.println("Transaction saved successfully");
        }
        catch (Exception e){
            System.out.println("Error while adding the transaction");
        }
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
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length == 5) { // Check for correct number of columns
                    try {
                        LocalDate date = LocalDate.parse(parts[0], dateFormatter);
                        double amount = Double.parseDouble(parts[1]);
                        TransactionCategory category = TransactionCategory.valueOf(parts[2].toUpperCase());
                        String description = parts[3];
                        TransactionType type = TransactionType.valueOf(parts[4].toUpperCase());
                        Transaction transaction = new Transaction(date, amount, category, description, type);
                        transactions.add(transaction);
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date: " + e.getMessage());
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Error parsing amount: " + e.getMessage());
                    }
                    catch (IllegalArgumentException e){
                        System.out.println("Error parsing  category/type: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid CSV format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    public void calculateBalance() {
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
    }

    public void generateCategoryReports(){
        Map<TransactionCategory, Double> categoryExpenses = transactions.stream()
                .filter(transaction -> transaction.type() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(Transaction::category, Collectors.summingDouble(Transaction::amount)));

        System.out.println("\n--- Category-wise Expenses ---");
        categoryExpenses.forEach((category, total) -> {
            System.out.println(category + ": " + total);
        });
    }
}
