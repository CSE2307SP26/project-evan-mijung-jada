package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private String name;
    private String owner;
    private double balance;
    private List<String> transactionHistory;
    private boolean open;
    private String type;

    public BankAccount() {
        this("Account", "Checking");
    }

    public BankAccount(String name, String type) {
        this.balance = 0;
        this.open = true;
        this.transactionHistory = new ArrayList<>();
        this.name = name;
        this.type = type;
        this.owner = "Unassigned";
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        if (!open) {
            throw new IllegalArgumentException("Account is closed.");
        }

        this.balance += amount;
        transactionHistory.add(
            String.format("Deposited $%.2f | New balance: $%.2f", amount, balance)
        );
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || !open) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        this.balance -= amount;

        transactionHistory.add(
            String.format("Withdrew $%.2f | New balance: $%.2f", amount, balance)
        );
        return true;
    }

    public void addInterestPayment(double amount) {
        if (amount <= 0 || !open) {
            throw new IllegalArgumentException("Invalid interest payment amount");
        }

        this.balance += amount;
        transactionHistory.add(
            String.format("Interest payment $%.2f | New balance: $%.2f", amount, balance)
        );
    }

    public void collectFee(double amount) {
        if (amount <= 0 || !open) {
            throw new IllegalArgumentException("Invalid fee amount");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        this.balance -= amount;
        transactionHistory.add(
            String.format("Fee charged $%.2f | New balance: $%.2f", amount, balance)
        );
    }

    public void closeAccount() {
        this.open = false;
    }

    public void reopenAccount() {
        this.open = true;
    }

    public boolean getStatus() {
        return this.open;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double amount) {
        this.balance = amount;
    }

    protected void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public List<String> getTransactionHistory() {
        return this.transactionHistory;
    }

    public String getTransactionHistoryText() {
        if (transactionHistory.isEmpty()) {
            return "No transactions found.";
        }

        StringBuilder historyText = new StringBuilder();
        for (String transaction : transactionHistory) {
            historyText.append(transaction).append("\n");
        }
        return historyText.toString();
    }

    public void transferMoney(BankAccount otherAccount, double amount) {
        if (amount <= this.balance && this.open) {
            this.balance -= amount;
            otherAccount.setBalance(otherAccount.getBalance() + amount);
            this.transactionHistory.add(
                String.format("Transferred $%.2f | New balance: $%.2f", amount, balance)
            );
            otherAccount.transactionHistory.add(
                String.format("$%.2f transferred to this account | New balance: $%.2f",
                    amount, otherAccount.getBalance())
            );
        } else {
            throw new IllegalArgumentException("Insufficient Funds");
        }
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            this.owner = "Unassigned";
        } else {
            this.owner = owner.trim();
        }
    }
}
