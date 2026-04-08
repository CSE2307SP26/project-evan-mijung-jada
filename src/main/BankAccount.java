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

<<<<<<< HEAD
        return  true;

=======
        return true;
>>>>>>> 966cc68 (added account status and reopen account features)
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
}
