import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class FinanceTracker {
    List<Transaction> transactions;
    final String DATA_FILE = "transactions.csv";

    public FinanceTracker() {
        transactions = new ArrayList<>();
    }

    public void saveTransactions(){
        try(FileWriter writer = new FileWriter(DATA_FILE)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for(Transaction transaction : transactions){
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
        }
        catch (IOException e){
            System.out.println("Error saving the transaction " +  e.getMessage());
        }
    }

    public void addTransaction(){
        try{
            Scanner  scanner = new Scanner(System.in);
            System.out.println("Enter the transaction Date: ");
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
            saveTransactions();
            System.out.println("Transaction saved successfully");
        }
        catch (Exception e){
            System.out.println("Error while adding the transaction");
        }
    }

    public void viewTransactions(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.printf("%-12s %-10s %-15s %-30s %-10s%n", "Date", "Amount", "Category", "Description", "Type");
        System.out.println("___________________________________________________________________________________");
        for(Transaction transaction : transactions){
            System.out.printf(
                    "%-12s %-10.2f %-15s %-30s %-10s%n",
                    transaction.date().format(dateTimeFormatter),
                    transaction.amount(),
                    transaction.category().name(),
                    transaction.description(),
                    transaction.type().name()
            );
        }
    }
}
