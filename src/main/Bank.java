package main;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private List<BankAccount> accounts;
    private List<UserProfile> userProfiles;

    public Bank() {
        this.accounts = new ArrayList<>();
        this.userProfiles = new ArrayList<>();
    }

    public void createAdditionalAccount(String type) {
        if(type.equals("checking")) {
            accounts.add(new CheckingAccount("Account " + (accounts.size() + 1)));
        } else {
            throw new IllegalArgumentException("Invalid account type.");
        }
    }

    public void createUserProfile(String username, String pin) {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        userProfiles.add(new UserProfile(username, pin));
    }

    public void createAccountForUser(String username, String type) {
        createAccountForUser(username, type, "Account " + (accounts.size() + 1));
    }

    public void createAccountForUser(String username, String type, String accountName) {
        String normalizedType = type == null ? "" : type.trim().toLowerCase();
        if ("savings".equals(normalizedType)) {
            throw new IllegalArgumentException("Savings accounts are not available yet.");
        }
        if (!"checking".equals(normalizedType)) {
            throw new IllegalArgumentException("Invalid account type.");
        }
        String safeName = (accountName == null || accountName.trim().isEmpty())
                ? "Account " + (accounts.size() + 1)
                : accountName.trim();
        BankAccount account = new CheckingAccount(safeName);
        account.setOwner(username);
        accounts.add(account);
    }

    public void createAdditionalAccountForUser(String username) {
        createAccountForUser(username, "checking");
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
    }

    public int getNumberOfUserProfiles() {
        return userProfiles.size();
    }

    public void createAdditionalAccount() {
        BankAccount account = new BankAccount("Account " + (accounts.size() + 1), "Checking");
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

    public String getAllAccountsSummary() {
        return getAllAccountsSummary(true);
    }

    public String getAllAccountsSummary(boolean includeOwner) {
        StringBuilder result = new StringBuilder();
        for (BankAccount account : accounts) {
            result.append(formatAccountSummary(account, includeOwner));
        }
        return result.toString();
    }

    public String getAccountsSummaryForUser(String username) {
        StringBuilder result = new StringBuilder();
        if (username == null) {
            return result.toString();
        }

        String normalized = username.trim();
        for (BankAccount account : accounts) {
            if (account.getOwner().equalsIgnoreCase(normalized)) {
                result.append(formatAccountSummary(account, false));
            }
        }

        return result.toString();
    }

    private String formatAccountSummary(BankAccount account, boolean includeOwner) {
        String status = account.getStatus() ? "Open" : "Closed";
        StringBuilder line = new StringBuilder();
        if (includeOwner) {
            line.append("User ")
                .append(account.getOwner())
                .append(" | ");
        }
        line.append(account.getName())
            .append(" - ")
            .append(account.getType())
            .append(" - ")
            .append(status)
            .append(": Balance = $")
            .append(String.format("%.2f", account.getBalance()))
            .append("\n");
        return line.toString();
    }
}