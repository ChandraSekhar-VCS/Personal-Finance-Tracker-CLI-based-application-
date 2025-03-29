import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            System.out.println("4. Balance");
            System.out.println("5. Category Reports");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            short choice = sc.nextShort();
            sc.nextLine();

            switch (choice){
                case 1:
                    tracker.addTransaction();
                    break;
                case 2:
                    System.out.println("1. View all transactions");
                    System.out.println("2. View transactions by date range");
                    System.out.print("Enter your choice: ");
                    String viewChoice = sc.nextLine();

                    LocalDate viewStartDate = null;
                    LocalDate viewEndDate = null;

                    if (viewChoice.equals("2")) {
                        boolean validStartDate = false;
                        while (!validStartDate) {
                            System.out.print("Enter start date (yyyy-MM-dd): ");
                            try {
                                viewStartDate = LocalDate.parse(sc.nextLine(), dateTimeFormatter);
                                validStartDate = true;
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            }
                        }

                        boolean validEndDate = false;
                        while (!validEndDate) {
                            System.out.print("Enter end date (yyyy-MM-dd): ");
                            try {
                                viewEndDate = LocalDate.parse(sc.nextLine(), dateTimeFormatter);
                                validEndDate = true;
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            }
                        }
                    }

                    tracker.viewTransactions(viewStartDate, viewEndDate);
                    break;

                case 3:
                    System.out.println("1. Generate an all time summary");
                    System.out.println("2. Generate a summary for custom date range");
                    System.out.print("Enter your choice: ");
                    String summaryChoice = sc.nextLine();
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    TransactionCategory category = null;
                    if (summaryChoice.equals("2")) {
                        boolean validStartDate = false;
                        while (!validStartDate) {
                            System.out.print("Enter start date (yyyy-MM-dd): ");
                            try {
                                startDate = LocalDate.parse(sc.nextLine(), dateTimeFormatter);
                                validStartDate = true;
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            }
                        }

                        boolean validEndDate = false;
                        while (!validEndDate) {
                            System.out.print("Enter end date (yyyy-MM-dd): ");
                            try {
                                endDate = LocalDate.parse(sc.nextLine(), dateTimeFormatter);
                                validEndDate = true;
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            }
                        }
                    }
                    System.out.print("Filter by category? (y/n): ");
                    String categoryFilter = sc.nextLine().toLowerCase();
                    if (categoryFilter.equals("y")) {
                        System.out.println("Enter category (GROCERIES,RENT,SALARY,ENTERTAINMENT,UTILITIES,TRANSPORTATION,OTHER): ");
                        String categoryChoice = sc.nextLine().toUpperCase();
                        try {
                            category = TransactionCategory.valueOf(categoryChoice);
                        } catch (IllegalArgumentException e) {
                            System.out.println("You have entered an invalid category");
                            continue;
                        }
                    }
                    tracker.generateSummary(startDate, endDate, category);
                    break;
                case 4:
                    tracker.calculateBalance();
                    break;
                case 5:
                    tracker.generateCategoryReports();
                    break;
                case 6:
                    System.out.println("Exiting...");
                sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }

        }
    }
}
