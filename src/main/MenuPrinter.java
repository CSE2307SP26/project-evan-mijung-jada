package main;

public class MenuPrinter {

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from an account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Create an additional account");
        System.out.println("6. Close an existing account");
        System.out.println("7. Transfer money from one account to another");
        System.out.println("8. Add interest payment to an account");
        System.out.println("9. Collect fee from an account");
        System.out.println("10. Rename an existing account");
        System.out.println("11. View all accounts");
        System.out.println("12. Check account status (open or closed)");
        System.out.println("13. Reopen a closed account");
        System.out.println("14. Exit the app");
    }

    public void displayAccountSelection(Bank bank) {
        int max = bank.getNumberOfAccounts();

        System.out.println("Select an account:");
        for (int i = 0; i < max; i++) {
            System.out.println((i + 1) + ": " + bank.getAccount(i).getName());
        }
    }
}
