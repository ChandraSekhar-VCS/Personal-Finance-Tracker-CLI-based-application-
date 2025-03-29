import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FinanceTracker tracker = new FinanceTracker();

        while(true){
            System.out.println("\n---Personal Finance Tracker---");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3.Exit");
            System.out.println("Enter your choice: ");

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
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }

        }
    }
}
