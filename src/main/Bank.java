package main;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<BankAccount> accounts;
    private List<UserProfile> userProfiles;

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
        this.accounts.add(new BankAccount("Account " + (accounts.size() + 1))); // start with one default account
        this.userProfiles = new ArrayList<>();
    }

    public void createUserProfile(String username, String pin) {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        userProfiles.add(new UserProfile(username, pin));
        createAccountForUser(username);
    }

    private void createAccountForUser(String username) {
        BankAccount account = new BankAccount("Account " + (accounts.size() + 1));
        account.setOwner(username);
        accounts.add(account);
    }

    public void createAdditionalAccountForUser(String username) {
        createAccountForUser(username);
    }

    public int findFirstAccountIndexForUser(String username) {
        if (username == null) {
            return -1;
        }

        String normalized = username.trim();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getOwner().equalsIgnoreCase(normalized)) {
                return i;
            }
        }
        return -1;
    }

    public List<Integer> getAccountIndexesForUser(String username) {
        List<Integer> indexes = new ArrayList<>();
        if (username == null) {
            return indexes;
        }

        String normalized = username.trim();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getOwner().equalsIgnoreCase(normalized)) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    public boolean authenticateUser(String username, String pin) {
        if (username == null || pin == null) {
            return false;
        }

        String normalized = username.trim();
        for (UserProfile profile : userProfiles) {
            if (profile.getUsername().equalsIgnoreCase(normalized)
                    && profile.matchesPin(pin)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUsernameTaken(String username) {
        if (username == null) {
            return false;
        }

        String normalized = username.trim();
        for (UserProfile profile : userProfiles) {
            if (profile.getUsername().equalsIgnoreCase(normalized)) {
                return true;
            }
        }
        return false;
        userProfiles.add(new UserProfile(username, pin));
    }

    public int getNumberOfUserProfiles() {
        return userProfiles.size();
    }

    public void createAdditionalAccount() {
        BankAccount account = new BankAccount("Account " + (accounts.size() + 1));
        account.setOwner("Unassigned");
        accounts.add(account);
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

    public void setWithdrawalLimit(int index, double limit) {
        BankAccount account = getAccount(index);
        account.setWithdrawalLimit(limit);
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