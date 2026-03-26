package main;

import java.util.Scanner;

public class MainMenu {

    private static final int DEPOSIT_SELECTION = 1;
    private static final int WITHDRAW_SELECTION = 2;
    private static final int CHECK_BALANCE_SELECTION = 3;
    private static final int VIEW_HISTORY_SELECTION = 4;
    private static final int CREATE_ACCOUNT_SELECTION = 5;
    private static final int CLOSE_ACCOUNT_SELECTION = 6;
    private static final int TRANSFER_SELECTION = 7;
    private static final int EXIT_SELECTION = 8;

    private static final int MAX_SELECTION = 8;

    private Bank bank;
    private Scanner keyboardInput;

    public MainMenu() {
        this.bank = new Bank();
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        System.out.println("1. Make a deposit");
        System.out.println("2. Withdraw from an account");
        System.out.println("3. Check account balance");
        System.out.println("4. View transaction history");
        System.out.println("5. Create an additional account");
        System.out.println("6. Close an existing account");
        System.out.println("7. Transfer money from one account to another");
        System.out.println("8. Exit the app");
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public int getAccountSelection() {
        int max = bank.getNumberOfAccounts();

        System.out.println("Select an account:");
        for (int i = 0; i < max; i++) {
            System.out.println((i + 1) + ". Account " + (i + 1));
        }

        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Choose account number: ");
            selection = keyboardInput.nextInt();
        }

        return selection - 1;
    }

    public void processInput(int selection) {
        switch (selection) {
            case DEPOSIT_SELECTION:
                performDeposit();
                break;
            case WITHDRAW_SELECTION:
                performWithdraw();
                break;
            case CHECK_BALANCE_SELECTION:
                checkBalance();
                break;
            case VIEW_HISTORY_SELECTION:
                viewTransactionHistory();
                break;
            case CREATE_ACCOUNT_SELECTION:
                createAdditionalAccount();
                break;
            case CLOSE_ACCOUNT_SELECTION:
                closeAccount();
                break;
            case TRANSFER_SELECTION:
                performTransfer();
                break;
            case EXIT_SELECTION:
                System.out.println("Exiting app...");
                break;
            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    public void performDeposit() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        double depositAmount = -1;
        while (depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextDouble();
        }

        selectedAccount.deposit(depositAmount);
        System.out.println("Deposit complete.");
    }

    public void performWithdraw() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        double withdrawAmount = -1;
        while (withdrawAmount < 0) {
            System.out.println("How much would you like to withdraw? Your current balance is "
                    + selectedAccount.getBalance());
            System.out.print("Enter an amount: ");
            withdrawAmount = keyboardInput.nextDouble();
        }

        try {
            selectedAccount.withdraw(withdrawAmount);
            System.out.println("Withdrawal complete.");
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    public void checkBalance() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        System.out.println("Current balance: $" + selectedAccount.getBalance());
    }

    public void viewTransactionHistory() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        System.out.println("\nTransaction History:");
        System.out.println(selectedAccount.getTransactionHistoryText());
    }

    public void createAdditionalAccount() {
        bank.createAdditionalAccount();
        System.out.println("New account created successfully.");
        System.out.println("Total accounts: " + bank.getNumberOfAccounts());
    }

    public void closeAccount() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        selectedAccount.closeAccount();
        System.out.println("Account closed.");
    }

    public void performTransfer() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        System.out.println("Which account would you like to transfer money to?");
        int accountToTransferToIndex = getAccountSelection();
        BankAccount accountToTransferTo = bank.getAccount(accountToTransferToIndex);

        double transferAmount = -1;
        while (transferAmount < 0) {
            System.out.println("How much would you like to transfer? Your current balance is "
                    + selectedAccount.getBalance());
            System.out.print("Enter an amount: ");
            transferAmount = keyboardInput.nextDouble();
        }

        selectedAccount.transferMoney(accountToTransferTo, transferAmount);
        System.out.println("Transfer complete");
    }

    public void run() {
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
