package main;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<BankAccount> accounts;

    public Bank() {
        this.accounts = new ArrayList<>();
        this.accounts.add(new BankAccount("Account " + (accounts.size() + 1), "Checking")); // start with one default account
    }

    public void createAdditionalAccount(String type) {
        if(type.equals("checking")) {
            accounts.add(new CheckingAccount("Account " + (accounts.size() + 1)));
        } else {
            throw new IllegalArgumentException("Invalid account type.");
        }
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

    public void collectFee(int index, double amount) {
        BankAccount account = getAccount(index);
        account.collectFee(amount);
    }

    public String getAllAccountsSummary() {
        StringBuilder result = new StringBuilder();

        for (BankAccount account : accounts) {
            if (account.getStatus()) {
                result.append(account.getName())
                      .append(" | " + account.getType())
                      .append(": Balance = $")
                      .append(String.format("%.2f", account.getBalance()))
                      .append("\n");
            }
        }

        return result.toString();
    }
}