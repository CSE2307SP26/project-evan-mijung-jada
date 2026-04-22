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
    private double withdrawalLimit;

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
        this.withdrawalLimit = 20.0;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        if (!open) {
            throw new IllegalArgumentException("Account is closed.");
        }

        balance += amount;
        transactionHistory.add(
            String.format("Deposited $%.2f | New balance: $%.2f", amount, balance)
        );
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0.");
        }
        if (!open) {
            throw new IllegalArgumentException("Account is closed.");
        }
        if (amount > withdrawalLimit) {
            throw new IllegalArgumentException(
                "Amount exceeds withdrawal limit of $" + withdrawalLimit
            );
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance -= amount;
        transactionHistory.add(
            String.format("Withdrew $%.2f | New balance: $%.2f", amount, balance)
        );
        return true;
    }

    public void addInterestPayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid interest payment amount");
        }
        if (!open) {
            throw new IllegalArgumentException("Account is closed.");
        }

        balance += amount;
        transactionHistory.add(
            String.format("Interest payment $%.2f | New balance: $%.2f", amount, balance)
        );
    }

    public void collectFee(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid fee amount");
        }
        if (!open) {
            throw new IllegalArgumentException("Account is closed.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance -= amount;
        transactionHistory.add(
            String.format("Fee charged $%.2f | New balance: $%.2f", amount, balance)
        );
    }

    public void closeAccount() {
        open = false;
    }

    public void reopenAccount() {
        open = true;
    }

    public boolean getStatus() {
        return open;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double amount) {
        balance = amount;
    }

    protected void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
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
        if (!open) {
            throw new IllegalArgumentException("Account is closed.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance -= amount;
        otherAccount.setBalance(otherAccount.getBalance() + amount);

        transactionHistory.add(
            String.format("Transferred $%.2f | New balance: $%.2f", amount, balance)
        );

        otherAccount.transactionHistory.add(
            String.format("$%.2f transferred to this account | New balance: $%.2f",
                amount, otherAccount.getBalance())
        );
    }

    public void setWithdrawalLimit(double limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }

        withdrawalLimit = limit;
        transactionHistory.add(
            String.format("Withdrawal limit set to $%.2f", limit)
        );
    }

    public double getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void rename(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
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