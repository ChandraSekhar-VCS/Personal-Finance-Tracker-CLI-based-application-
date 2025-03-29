import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FinanceTracker tracker = new FinanceTracker();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while(true){
            System.out.println("\n---Personal Finance Tracker---");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Summary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            short choice = sc.nextShort();
            sc.nextLine();

            switch (choice){
                case 1:
                    tracker.addTransaction();
                    break;
                case 2:
                    tracker.viewTransactions();
                    break;
                case 3:
                    System.out.println("1. Generate an all time summary");
                    System.out.println("2. Generate a summary for custom date range");
                    System.out.print("Enter your choice: ");
                    String summaryChoice = sc.nextLine();
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    TransactionCategory category = null;
                    if(summaryChoice.equals("2")){
                        System.out.print("Enter start date (yyyy-mm-dd): ");
                        String sDate  = sc.nextLine();
                        startDate = LocalDate.parse(sDate,dateTimeFormatter);
                        System.out.print("Enter end date (yyyy-mm-dd): ");
                        String eDate = sc.nextLine();
                        endDate = LocalDate.parse(eDate,dateTimeFormatter);
                    }
                    System.out.print("Filter by categoty? (y/n): ");
                    String categoryFilter = sc.nextLine().toLowerCase();
                    if(categoryFilter.equals("y")){
                        System.out.println("Enter category (GROCERIES,RENT,SALARY,ENTERTAINMENT,UTILITIES,TRANSPORTATION,OTHER): ");
                        String categoryChoice =  sc.nextLine().toUpperCase();
                        try{
                            category = TransactionCategory.valueOf(categoryChoice);
                        }
                        catch(IllegalArgumentException e){
                            System.out.println("You have entered a invalid category");
                            continue;
                        }
                    }
                    tracker.generateSummary(startDate,endDate,category);
                    break;
                case 4:
                    System.out.println("Exiting...");
                sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }

        }
    }
}
