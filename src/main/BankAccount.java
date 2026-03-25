package main;

import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    private double balance;
    private List<String> transactionHistory;
    private boolean open;

    public BankAccount() {
        this.balance = 0;
        this.open = true;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount > 0 && open) {
            this.balance += amount;
            transactionHistory.add(
                String.format("Deposited $%.2f | New balance: $%.2f", amount, balance)
            );
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void closeAccount() {
        this.open = false;
    }

    public boolean getStatus() {
        return this.open;
    }

    public double getBalance() {
        return this.balance;
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
}