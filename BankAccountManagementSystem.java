import java.io.*;
import java.util.*;

public class BankAccountManagementSystem {

    // BankAccount class represents a bank account
    static class BankAccount {
        String accountId;
        String accountHolder;
        double balance;

        public BankAccount(String accountId, String accountHolder, double balance) {
            this.accountId = accountId;
            this.accountHolder = accountHolder;
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "Account ID: " + accountId + ", Account Holder: " + accountHolder + ", Balance: $" + balance;
        }

        // Method to deposit money into the account
        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
            } else {
                System.out.println("Deposit amount must be positive.");
            }
        }

        // Method to withdraw money from the account
        public boolean withdraw(double amount) {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
                return true;
            } else {
                System.out.println("Insufficient funds or invalid amount.");
                return false;
            }
        }

        // Method to transfer money between two accounts
        public boolean transferTo(BankAccount destination, double amount) {
            if (this.withdraw(amount)) {
                destination.deposit(amount);
                return true;
            }
            return false;
        }

        // Check balance of the account
        public double checkBalance() {
            return balance;
        }
    }

    // Bank class represents the collection of accounts
    static class Bank {
        Map<String, BankAccount> accounts = new HashMap<>();
        String filePath = "accounts.txt"; // File to store account data

        // Load accounts from file
        public void loadAccounts() {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 3) {
                        String accountId = data[0].trim();
                        String accountHolder = data[1].trim();
                        double balance = Double.parseDouble(data[2].trim());
                        BankAccount account = new BankAccount(accountId, accountHolder, balance);
                        accounts.put(accountId, account);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading accounts: " + e.getMessage());
            }
        }

        // Save accounts to file
        public void saveAccounts() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (BankAccount account : accounts.values()) {
                    writer.write(account.accountId + "," + account.accountHolder + "," + account.balance);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving accounts: " + e.getMessage());
            }
        }

        // Add a new account
        public void addAccount(String accountId, String accountHolder, double balance) {
            BankAccount account = new BankAccount(accountId, accountHolder, balance);
            accounts.put(accountId, account);
            saveAccounts();
        }

        // Get an account by ID
        public BankAccount getAccount(String accountId) {
            return accounts.get(accountId);
        }

        // List all accounts
        public void listAccounts() {
            if (accounts.isEmpty()) {
                System.out.println("No accounts available.");
            } else {
                for (BankAccount account : accounts.values()) {
                    System.out.println(account);
                }
            }
        }
    }

    // Main class for user interaction
    public static class BankApp {
        static Scanner scanner = new Scanner(System.in);
        static Bank bank = new Bank();

        public static void main(String[] args) {
            bank.loadAccounts(); // Load accounts from file

            while (true) {
                displayMenu();
                int choice = getChoice();
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        depositMoney();
                        break;
                    case 3:
                        withdrawMoney();
                        break;
                    case 4:
                        checkBalance();
                        break;
                    case 5:
                        transferMoney();
                        break;
                    case 6:
                        listAccounts();
                        break;
                    case 7:
                        System.out.println("Exiting the application.");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        }

        // Display menu options
        private static void displayMenu() {
            System.out.println("\nBank Account Management System");
            System.out.println("1. Create a new account");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. Check balance");
            System.out.println("5. Transfer money");
            System.out.println("6. List all accounts");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
        }

        // Get user input for menu choice
        private static int getChoice() {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                return -1; // Return an invalid choice
            }
        }

        // Create a new account
        private static void createAccount() {
            System.out.print("Enter Account ID: ");
            String accountId = scanner.nextLine();

            System.out.print("Enter Account Holder Name: ");
            String accountHolder = scanner.nextLine();

            double balance = getValidAmount("Enter initial deposit amount: $");

            bank.addAccount(accountId, accountHolder, balance);
            System.out.println("Account created successfully!");
        }

        // Deposit money into an account
        private static void depositMoney() {
            System.out.print("Enter Account ID: ");
            String accountId = scanner.nextLine();
            BankAccount account = bank.getAccount(accountId);

            if (account != null) {
                double amount = getValidAmount("Enter amount to deposit: $");
                account.deposit(amount);
                System.out.println("Amount deposited successfully!");
            } else {
                System.out.println("Account not found.");
            }
        }

        // Withdraw money from an account
        private static void withdrawMoney() {
            System.out.print("Enter Account ID: ");
            String accountId = scanner.nextLine();
            BankAccount account = bank.getAccount(accountId);

            if (account != null) {
                double amount = getValidAmount("Enter amount to withdraw: $");
                if (account.withdraw(amount)) {
                    System.out.println("Amount withdrawn successfully!");
                }
            } else {
                System.out.println("Account not found.");
            }
        }

        // Check balance of an account
        private static void checkBalance() {
            System.out.print("Enter Account ID: ");
            String accountId = scanner.nextLine();
            BankAccount account = bank.getAccount(accountId);

            if (account != null) {
                System.out.println("Balance: $" + account.checkBalance());
            } else {
                System.out.println("Account not found.");
            }
        }

        // Transfer money between two accounts
        private static void transferMoney() {
            System.out.print("Enter source Account ID: ");
            String sourceAccountId = scanner.nextLine();
            BankAccount sourceAccount = bank.getAccount(sourceAccountId);

            if (sourceAccount != null) {
                System.out.print("Enter destination Account ID: ");
                String destAccountId = scanner.nextLine();
                BankAccount destAccount = bank.getAccount(destAccountId);

                if (destAccount != null) {
                    double amount = getValidAmount("Enter amount to transfer: $");
                    if (sourceAccount.transferTo(destAccount, amount)) {
                        System.out.println("Money transferred successfully!");
                    } else {
                        System.out.println("Transfer failed.");
                    }
                } else {
                    System.out.println("Destination acco
