package main;

public class MenuPrinter {

    public void displayNoAccountOptions(String username) {
        printHeader("Welcome " + username + ", you don't have an account with us.\nCreate one here.");
        System.out.println("1. Create a checking account");
        System.out.println("2. Create a savings account");
        System.out.println("3. Logout");
        System.out.println("4. Exit the app");
    }

    public void displayAccountSelectionHeader(String username) {
        printHeader("Welcome " + username + "\nSelect an account");
    }

    public void displayCustomerAccountMenu(String username, String accountName) {
        printHeader("Welcome " + username + "\nCurrent account: " + accountName);
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Check balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Transfer money");
        System.out.println("6. Rename account");
        System.out.println("7. Close account");
        System.out.println("8. Reopen account");
        System.out.println("9. Check account status");
        System.out.println("10. Open a new account");
        System.out.println("11. View all my accounts");
        System.out.println("12. Switch account");
        System.out.println("13. Logout");
        System.out.println("14. Exit app");
    }

    public void displayAdminMenu() {
        printHeader("Administrator Menu");
        System.out.println("1. Add interest payment to an account");
        System.out.println("2. Collect fee from an account");
        System.out.println("3. View all accounts");
        System.out.println("4. Search accounts by username");
        System.out.println("5. Exit admin mode");
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

    public void displayAccountSelection(Bank bank, java.util.List<Integer> indexes, boolean showOwner) {
        System.out.println("Select an account:");
        if (indexes.isEmpty()) {
            System.out.println("No accounts available.");
            return;
        }
        for (int i = 0; i < indexes.size(); i++) {
            BankAccount account = bank.getAccount(indexes.get(i));
            String status = account.getStatus() ? "Open" : "Closed";
            if (showOwner) {
                System.out.println((i + 1) + ": " + account.getName()
                        + " - " + account.getType() + " - " + status
                        + " | User " + account.getOwner());
            } else {
                System.out.println((i + 1) + ": " + account.getName()
                        + " - " + account.getType() + " - " + status);
            }
        }
    }
}
