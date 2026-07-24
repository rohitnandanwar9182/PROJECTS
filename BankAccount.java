import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ---------- Custom Exceptions ----------

class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

// ---------- Transaction Record ----------

class Transaction {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s %10.2f | Balance after: %10.2f",
                timestamp.format(FORMAT), type, amount, balanceAfter);
    }
}

// ---------- Account Hierarchy ----------

abstract class Account {
    private final String accountId;
    private final String ownerName;
    protected double balance;
    private final List<Transaction> history = new ArrayList<>();

    public Account(String accountId, String ownerName, double openingBalance) {
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        if (openingBalance > 0) {
            history.add(new Transaction("OPEN", openingBalance, balance));
        }
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, balance));
    }

    // Withdraw rules differ per account type (overdraft vs strict), so subclasses implement it.
    public abstract void withdraw(double amount);

    protected void recordWithdrawal(double amount) {
        history.add(new Transaction("WITHDRAW", amount, balance));
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void printStatement() {
        System.out.println("\n--- Statement for " + accountId + " (" + ownerName + ") ---");
        System.out.println("Type: " + this.getClass().getSimpleName());
        for (Transaction t : history) {
            System.out.println("  " + t);
        }
        System.out.printf("Current Balance: %.2f%n", balance);
    }
}

class SavingsAccount extends Account {
    private final double interestRate; // annual, e.g. 0.04 = 4%
    private final double minimumBalance;

    public SavingsAccount(String accountId, String ownerName, double openingBalance,
                           double interestRate, double minimumBalance) {
        super(accountId, ownerName, openingBalance);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (balance - amount < minimumBalance) {
            throw new InsufficientFundsException(
                    "Withdrawal would breach minimum balance of " + minimumBalance);
        }
        balance -= amount;
        recordWithdrawal(amount);
    }

    public void applyMonthlyInterest() {
        double monthlyInterest = balance * (interestRate / 12);
        balance += monthlyInterest;
        getHistory().add(new Transaction("INTEREST", monthlyInterest, balance));
    }
}

class CheckingAccount extends Account {
    private final double overdraftLimit;

    public CheckingAccount(String accountId, String ownerName, double openingBalance, double overdraftLimit) {
        super(accountId, ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (balance - amount < -overdraftLimit) {
            throw new InsufficientFundsException(
                    "Withdrawal exceeds overdraft limit of " + overdraftLimit);
        }
        balance -= amount;
        recordWithdrawal(amount);
    }
}

// ---------- Bank: manages multiple accounts ----------

class Bank {
    private final Map<String, Account> accounts = new HashMap<>();
    private int nextId = 1001;

    public String openSavingsAccount(String ownerName, double openingBalance,
                                      double interestRate, double minimumBalance) {
        String id = "SAV-" + (nextId++);
        accounts.put(id, new SavingsAccount(id, ownerName, openingBalance, interestRate, minimumBalance));
        return id;
    }

    public String openCheckingAccount(String ownerName, double openingBalance, double overdraftLimit) {
        String id = "CHK-" + (nextId++);
        accounts.put(id, new CheckingAccount(id, ownerName, openingBalance, overdraftLimit));
        return id;
    }

    public Account getAccount(String accountId) {
        Account acc = accounts.get(accountId);
        if (acc == null) {
            throw new AccountNotFoundException("No account found with ID: " + accountId);
        }
        return acc;
    }

    public void transfer(String fromId, String toId, double amount) {
        Account from = getAccount(fromId);
        Account to = getAccount(toId);
        from.withdraw(amount);   // rolls back naturally: if this throws, 'to' is never touched
        to.deposit(amount);
    }

    public void applyInterestToAllSavings() {
        for (Account acc : accounts.values()) {
            if (acc instanceof SavingsAccount) {
                ((SavingsAccount) acc).applyMonthlyInterest();
            }
        }
    }

    public double getTotalBankAssets() {
        double total = 0;
        for (Account acc : accounts.values()) {
            total += acc.getBalance();
        }
        return total;
    }
}

// ---------- Interactive Console Driver ----------

public class BankSystem {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Bank bank = new Bank();

        System.out.println("=== Welcome to the Bank System ===");
        System.out.print("Enter your name to open a Savings Account: ");
        String name = scanner.nextLine();

        System.out.print("Enter opening balance: ");
        double openingBalance = readAmount(scanner);

        String accountId = bank.openSavingsAccount(name, openingBalance, 0.04, 500);
        Account account = bank.getAccount(accountId);
        System.out.println("Account created! Your Account ID is: " + accountId);

        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Print Statement");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.printf("Current Balance: %.2f%n", account.getBalance());
                    break;

                case "2":
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = readAmount(scanner);
                    try {
                        account.deposit(depositAmount);
                        System.out.printf("Deposit successful. New Balance: %.2f%n", account.getBalance());
                    } catch (InvalidAmountException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "3":
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = readAmount(scanner);
                    try {
                        account.withdraw(withdrawAmount);
                        System.out.printf("Withdrawal successful. New Balance: %.2f%n", account.getBalance());
                    } catch (InvalidAmountException | InsufficientFundsException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case "4":
                    account.printStatement();
                    break;

                case "5":
                    System.out.println("Thank you for banking with us. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option, please choose between 1 and 5.");
            }
        }

        scanner.close();
    }

    // Keeps asking until the user enters a valid number
    private static double readAmount(java.util.Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
