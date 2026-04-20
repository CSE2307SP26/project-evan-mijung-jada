package main;

import java.util.Scanner;

public class CustomerMenu {

    private static final int DEPOSIT_SELECTION = 1;
    private static final int WITHDRAW_SELECTION = 2;
    private static final int CHECK_BALANCE_SELECTION = 3;
    private static final int VIEW_HISTORY_SELECTION = 4;
    private static final int CREATE_ACCOUNT_SELECTION = 5;
    private static final int CLOSE_ACCOUNT_SELECTION = 6;
    private static final int TRANSFER_SELECTION = 7;
    private static final int RENAME_SELECTION = 8;
    private static final int VIEW_ACCOUNTS_SELECTION = 9;
    private static final int CHECK_ACCOUNT_STATUS_SELECTION = 10;
    private static final int REOPEN_ACCOUNT_SELECTION = 11;
    private static final int CREATE_PROFILE_SELECTION = 16;

    private final Bank bank;
    private final Scanner keyboardInput;
    private final AccountSelector accountSelector;

    public CustomerMenu(Bank bank, Scanner keyboardInput, AccountSelector accountSelector) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
        this.accountSelector = accountSelector;
    }

    public boolean handleSelection(int selection) {
        switch (selection) {
            case DEPOSIT_SELECTION:
                performDeposit();
                return true;
            case WITHDRAW_SELECTION:
                performWithdraw();
                return true;
            case CHECK_BALANCE_SELECTION:
                checkBalance();
                return true;
            case VIEW_HISTORY_SELECTION:
                viewTransactionHistory();
                return true;
            case CREATE_ACCOUNT_SELECTION:
                createAdditionalAccount();
                return true;
            case CLOSE_ACCOUNT_SELECTION:
                closeAccount();
                return true;
            case TRANSFER_SELECTION:
                performTransfer();
                return true;
            case RENAME_SELECTION:
                renameAccount();
                return true;
            case VIEW_ACCOUNTS_SELECTION:
                viewAllAccounts();
                return true;
            case CHECK_ACCOUNT_STATUS_SELECTION:
                checkAccountStatus();
                return true;
            case REOPEN_ACCOUNT_SELECTION:
                reopenAccount();
                return true;
            case CREATE_PROFILE_SELECTION:
                createUserProfile();
                return true;
            default:
                return false;
        }
    }

    private void performDeposit() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        double depositAmount = readPositiveAmount("How much would you like to deposit: ");

        try {
            selectedAccount.deposit(depositAmount);
            System.out.println("Deposit complete.");
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    private void performWithdraw() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        System.out.println("How much would you like to withdraw? Your current balance is "
                + selectedAccount.getBalance());
        double withdrawAmount = readPositiveAmount("Enter an amount: ");

        try {
            selectedAccount.withdraw(withdrawAmount);
            System.out.println("Withdrawal complete.");
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    private void checkBalance() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        System.out.println("Current balance: $" + selectedAccount.getBalance());
    }

    private void viewTransactionHistory() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        System.out.println("\nTransaction History:");
        System.out.println(selectedAccount.getTransactionHistoryText());
    }

    private void createAdditionalAccount() {
        bank.createAdditionalAccount();
        System.out.println("New account created successfully.");
        System.out.println("Total accounts: " + bank.getNumberOfAccounts());
    }

    private void closeAccount() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        selectedAccount.closeAccount();
        System.out.println("Account closed.");
    }

    private void performTransfer() {
        BankAccount fromAccount = bank.getAccount(accountSelector.selectAccount());
        System.out.println("Which account would you like to transfer money to?");
        BankAccount toAccount = bank.getAccount(accountSelector.selectAccount());
        double transferAmount = readPositiveAmount("Enter an amount: ");

        fromAccount.transferMoney(toAccount, transferAmount);
        System.out.println("Transfer complete");
    }

    private void checkAccountStatus() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        boolean isOpen = selectedAccount.getStatus();
        System.out.println("Account status: " + (isOpen ? "Open" : "Closed"));
    }

    private void reopenAccount() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        if (selectedAccount.getStatus()) {
            System.out.println("Account is already open.");
            return;
        }

        selectedAccount.reopenAccount();
        System.out.println("Account reopened successfully.");
    }

    private void renameAccount() {
        BankAccount selectedAccount = bank.getAccount(accountSelector.selectAccount());
        System.out.print("Enter your new account name: ");
        String newName = keyboardInput.nextLine();
        selectedAccount.rename(newName);
    }

    private void viewAllAccounts() {
        System.out.println("\nAll Accounts Summary:");
        System.out.println(bank.getAllAccountsSummary());
    }

    private void createUserProfile() {
        String username = readNonBlank("Enter a username: ", "Username cannot be empty.");
        String pin = readPin();
        bank.createUserProfile(username, pin);
        System.out.println("User profile created.");
    }

    private String readNonBlank(String prompt, String errorMessage) {
        String value = "";
        while (value.trim().isEmpty()) {
            System.out.print(prompt);
            value = keyboardInput.nextLine();
            if (value.trim().isEmpty()) {
                System.out.println(errorMessage);
            }
        }
        return value.trim();
    }

    private String readPin() {
        String pin = "";
        while (!pin.matches("\\d{4}")) {
            System.out.print("Enter a 4-digit PIN: ");
            pin = keyboardInput.nextLine();
            if (!pin.matches("\\d{4}")) {
                System.out.println("PIN must be exactly 4 digits.");
            }
        }
        return pin;
    }

    private double readPositiveAmount(String prompt) {
        double amount = -1;
        while (amount <= 0) {
            System.out.print(prompt);
            amount = accountSelector.readDouble();
            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
            }
        }
        return amount;
    }
}
