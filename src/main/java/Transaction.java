import java.time.LocalDate;

public record Transaction(LocalDate date,double amount,TransactionCategory category, String description,TransactionType type) {}
