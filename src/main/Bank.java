package main;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<BankAccount> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
        this.accounts.add(new BankAccount("Account " + (accounts.size() + 1))); // start with one default account
    }

    public void createAdditionalAccount() {
        accounts.add(new BankAccount("Account " + (accounts.size() + 1)));
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public BankAccount getAccount(int index) {
        if (index < 0 || index >= accounts.size()) {
            throw new IllegalArgumentException("Invalid account index.");
        }
        return accounts.get(index);
    }

    public void addInterestPayment(int index, double amount) {
        BankAccount account = getAccount(index);
        account.addInterestPayment(amount);
    }

    public String getAllAccountsSummary() {
        StringBuilder result = new StringBuilder();

        for (BankAccount account : accounts) {
            if (account.getStatus()) {
                result.append(account.getName())
                      .append(": Balance = $")
                      .append(String.format("%.2f", account.getBalance()))
                      .append("\n");
            }
        }

        return result.toString();
    }
}