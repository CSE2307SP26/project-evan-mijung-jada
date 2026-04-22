package main;

public class MenuPrinter {

    public void displayOptions() {
        displayOptions(false);
    }

    public void displayOptions(boolean showAdminOptions) {
        printHeader("Welcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from an account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Create an additional account");
        System.out.println("6. Close an existing account");
        System.out.println("7. Transfer money from one account to another");
        System.out.println("8. Rename an existing account");
        System.out.println("9. View all accounts");
        System.out.println("10. Check account status (open or closed)");
        System.out.println("11. Reopen a closed account");
        System.out.println("12. Exit the app");
        if (showAdminOptions) {
            System.out.println("13. Add interest payment to an account");
            System.out.println("14. Collect fee from an account");
            System.out.println("15. Set withdrawal limit on an account");
        }
        System.out.println("16. Create a user profile");
    }

    public void displayCustomerOptions(String username, String accountName) {
        printHeader("Welcome " + username + ", you are at " + accountName);
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from an account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Create an additional account");
        System.out.println("6. Close " + accountName);
        System.out.println("7. Transfer money from one account to another");
        System.out.println("8. Rename " + accountName);
        System.out.println("9. View all accounts");
        System.out.println("10. Check account status for " + accountName);
        System.out.println("11. Reopen a closed account");
        System.out.println("12. Choose a different account");
        System.out.println("13. Exit the app");
    }

    public void displayAdminOptions() {
        printHeader("Welcome to the 237 Bank App!");
        System.out.println("1. Add interest payment to an account");
        System.out.println("2. Collect fee from an account");
        System.out.println("3. Set withdrawal limit on an account");
        System.out.println("4. View all accounts");
        System.out.println("5. Search accounts by username");
        System.out.println("6. Exit admin mode");
    }

    public void displayLoginOptions() {
        printHeader("Welcome to the 237 Bank App!");
        System.out.println("1. Create a user profile");
        System.out.println("2. Login with an existing profile");
        System.out.println("3. Login as admin");
        System.out.println("4. Exit the app");
    }

    private void printHeader(String title) {
        System.out.println("--------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------");
    }

    public void displayAccountSelection(Bank bank) {
        displayAccountSelection(bank, false);
    }

    public void displayAccountSelection(Bank bank, boolean showOwner) {
        int max = bank.getNumberOfAccounts();
        java.util.List<Integer> indexes = new java.util.ArrayList<>();
        for (int i = 0; i < max; i++) {
            indexes.add(i);
        }
        displayAccountSelection(bank, indexes, showOwner);
    }

    public void displayAccountSelection(
        Bank bank,
        java.util.List<Integer> indexes,
        boolean showOwner
    ) {
        System.out.println("Select an account:");
        if (indexes.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }

        for (int i = 0; i < indexes.size(); i++) {
            BankAccount account = bank.getAccount(indexes.get(i));
            if (showOwner) {
                System.out.println(
                    (i + 1) + ": User " + account.getOwner()
                        + " | Account " + account.getName()
                );
            } else {
                System.out.println((i + 1) + ": " + account.getName());
            }
        }
    }
}