package main;

import java.util.Scanner;

public class MenuController {

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
    private static final int EXIT_SELECTION = 12;
    private static final int ADMIN_MODE_SELECTION = 13;
    private static final int INTEREST_PAYMENT_SELECTION = 14;
    private static final int FEE_COLLECTION_SELECTION = 15;

    private static final int MAX_SELECTION = 15;

    private final Bank bank;
    private final Scanner keyboardInput;
    private final MenuPrinter printer;
    private boolean adminMode;

    public MenuController(Bank bank, Scanner keyboardInput, MenuPrinter printer) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
        this.printer = printer;
        this.adminMode = false;
    }

    public void run() {
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            printer.displayOptions(adminMode);
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
            System.out.println();
        }
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
            keyboardInput.nextLine();
        }
        return selection;
    }

    public int getAccountSelection() {
        int max = bank.getNumberOfAccounts();

        printer.displayAccountSelection(bank);

        int selection = -1;
        while (selection < 1 || selection > max) {
            try {
                System.out.print("Choose account number: ");
                selection = keyboardInput.nextInt();
                keyboardInput.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                keyboardInput.nextLine(); // Clear the invalid input
            }
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
            case CHECK_ACCOUNT_STATUS_SELECTION:
                checkAccountStatus();
                break;
            case REOPEN_ACCOUNT_SELECTION:
                reopenAccount();
                break;
            case EXIT_SELECTION:
                System.out.println("Exiting app...");
                break;
            case RENAME_SELECTION:
                renameAccount();
                break;
            case VIEW_ACCOUNTS_SELECTION:
                viewAllAccounts();
                break;
            case ADMIN_MODE_SELECTION:
                toggleAdminMode();
                break;
            case INTEREST_PAYMENT_SELECTION:
                if (ensureAdminAccess()) {
                    addInterestPayment();
                }
                break;
            case FEE_COLLECTION_SELECTION:
                if (ensureAdminAccess()) {
                    collectFee();
                }
                break;
            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    private boolean ensureAdminAccess() {
        if (adminMode) {
            return true;
        }

        System.out.println("Admin access required. Select option 13 to enable admin mode.");
        return false;
    }

    private void toggleAdminMode() {
        if (adminMode) {
            adminMode = false;
            System.out.println("Admin mode disabled.");
            return;
        }

        System.out.print("Enter passcode (0000): ");
        String passcode = keyboardInput.nextLine();
        if ("0000".equals(passcode)) {
            adminMode = true;
            System.out.println("Admin mode enabled.");
        } else {
            System.out.println("Invalid passcode.");
        }
    }

    public void performDeposit() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        while (true) {
            System.out.print("How much would you like to deposit: ");
            double depositAmount = keyboardInput.nextDouble();
            keyboardInput.nextLine();

            if (depositAmount <= 0) {
                System.out.println("Deposit amount must be greater than 0.");
                continue;
            }

            try {
                selectedAccount.deposit(depositAmount);
                System.out.println("Deposit complete.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Deposit failed: " + e.getMessage());
            }
        }
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
        try {
            System.out.print("Checking or Savings?: ");
            String type = keyboardInput.nextLine();
            bank.createAdditionalAccount(type.toLowerCase());
            System.out.println("New " + type + " account created successfully.");
            System.out.println("Total accounts: " + bank.getNumberOfAccounts());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create additional account: " + e.getMessage());
        }
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

    public void addInterestPayment() {
        int accountIndex = getAccountSelection();

        double interestAmount = -1;
        while (interestAmount <= 0) {
            System.out.print("Enter interest payment amount: ");
            interestAmount = keyboardInput.nextDouble();
            keyboardInput.nextLine();
        }

        bank.addInterestPayment(accountIndex, interestAmount);
        System.out.println("Interest payment applied.");
    }

    public void collectFee() {
        int accountIndex = getAccountSelection();

        double feeAmount = -1;
        while (feeAmount <= 0) {
            System.out.print("Enter fee amount: ");
            feeAmount = keyboardInput.nextDouble();
            keyboardInput.nextLine();
        }

        bank.collectFee(accountIndex, feeAmount);
        System.out.println("Fee collected.");
    }

    public void checkAccountStatus() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        boolean isOpen = selectedAccount.getStatus();
        System.out.println("Account status: " + (isOpen ? "Open" : "Closed"));
    }

    public void reopenAccount() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        boolean isOpen = selectedAccount.getStatus();

        if (isOpen) {
            System.out.println("Account is already open.");
            return;
        }

        selectedAccount.reopenAccount();
        System.out.println("Account reopened successfully.");
    }

    public void renameAccount() {
        int accountIndex = getAccountSelection();
        BankAccount selectedAccount = bank.getAccount(accountIndex);

        System.out.print("Enter your new account name: ");
        String newName = keyboardInput.nextLine();
        selectedAccount.rename(newName);
    }

    public void viewAllAccounts() {
        System.out.println("\nAll Accounts Summary:");
        System.out.println(bank.getAllAccountsSummary());
    }
}
