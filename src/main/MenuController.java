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
    private static final int CHOOSE_DIFFERENT_ACCOUNT_SELECTION = 12;
    private static final int EXIT_SELECTION = 13;
    private static final int INTEREST_PAYMENT_SELECTION = 13;
    private static final int FEE_COLLECTION_SELECTION = 14;

    private static final int ADMIN_INTEREST_SELECTION = 1;
    private static final int ADMIN_FEE_SELECTION = 2;
    private static final int ADMIN_EXIT_SELECTION = 3;

    private static final int MAX_CUSTOMER_SELECTION = 13;
    private static final int MAX_ADMIN_SELECTION = 3;

    private final Bank bank;
    private final Scanner keyboardInput;
    private final MenuPrinter printer;
    private boolean adminMode;
    private String currentUsername;
    private int currentAccountIndex;

    public MenuController(Bank bank, Scanner keyboardInput, MenuPrinter printer) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
        this.printer = printer;
        this.adminMode = false;
        this.currentUsername = null;
        this.currentAccountIndex = -1;
    }

    public void run() {
        run(false);
    }

    public void run(boolean adminMode) {
        this.adminMode = adminMode;
        if (adminMode) {
            runAdminLoop();
        } else {
            runCustomerLoop();
        }
    }

    private void runCustomerLoop() {
        if (!ensureCurrentAccount()) {
            return;
        }
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            BankAccount currentAccount = getCurrentAccount();
            printer.displayCustomerOptions(currentUsername, currentAccount.getName());
            selection = getUserSelection(MAX_CUSTOMER_SELECTION);
            processInput(selection);
            System.out.println();
        }
    }

    private void runAdminLoop() {
        int selection = -1;
        while (selection != ADMIN_EXIT_SELECTION) {
            printer.displayAdminOptions();
            selection = getUserSelection(MAX_ADMIN_SELECTION);
            processAdminInput(selection);
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

        printer.displayAccountSelection(bank, adminMode);

        if (max == 0) {
            return -1;
        }

        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Choose account number: ");
            selection = keyboardInput.nextInt();
            keyboardInput.nextLine();
        }

        return selection - 1;
    }

    private int getAccountSelection(java.util.List<Integer> indexes, boolean showOwner) {
        printer.displayAccountSelection(bank, indexes, showOwner);
        if (indexes.isEmpty()) {
            return -1;
        }

        int selection = -1;
        while (selection < 1 || selection > indexes.size()) {
            System.out.print("Choose account number: ");
            selection = keyboardInput.nextInt();
            keyboardInput.nextLine();
        }

        return indexes.get(selection - 1);
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
            case CHOOSE_DIFFERENT_ACCOUNT_SELECTION:
                chooseDifferentAccount();
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
            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    private void processAdminInput(int selection) {
        switch (selection) {
            case ADMIN_INTEREST_SELECTION:
                addInterestPayment();
                break;
            case ADMIN_FEE_SELECTION:
                collectFee();
                break;
            case ADMIN_EXIT_SELECTION:
                System.out.println("Exiting admin mode...");
                break;
            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    public boolean authenticateUser(String username, String pin) {
        return bank.authenticateUser(username, pin);
    }

    public boolean isUsernameTaken(String username) {
        return bank.isUsernameTaken(username);
    }

    public boolean authenticateAdmin(String passcode) {
        return "0000".equals(passcode);
    }

    public void createUserProfile() {
        while (true) {
            String username = readNonBlank("Enter a username: ");
            String pin = readPin();
            try {
                bank.createUserProfile(username, pin);
                System.out.println("User profile created.");
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String promptLine(String prompt) {
        System.out.print(prompt);
        return keyboardInput.nextLine();
    }

    private String readNonBlank(String prompt) {
        String value = "";
        while (value.trim().isEmpty()) {
            System.out.print(prompt);
            value = keyboardInput.nextLine();
            if (value.trim().isEmpty()) {
                System.out.println("Username cannot be empty.");
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

    public boolean setCurrentUser(String username) {
        int accountIndex = bank.findFirstAccountIndexForUser(username);
        if (accountIndex < 0) {
            return false;
        }
        this.currentUsername = username.trim();
        this.currentAccountIndex = accountIndex;
        return true;
    }

    private boolean ensureCurrentAccount() {
        if (currentUsername == null) {
            return false;
        }
        if (currentAccountIndex < 0) {
            return false;
        }
        return true;
    }

    private BankAccount getCurrentAccount() {
        if (!ensureCurrentAccount()) {
            return null;
        }
        return bank.getAccount(currentAccountIndex);
    }

    private java.util.List<Integer> getUserAccountIndexes() {
        return bank.getAccountIndexesForUser(currentUsername);
    }

    private void chooseDifferentAccount() {
        java.util.List<Integer> indexes = getUserAccountIndexes();
        if (indexes.size() <= 1) {
            System.out.println("No other accounts available.");
            return;
        }

        int selectedIndex = getAccountSelection(indexes, false);
        if (selectedIndex < 0) {
            return;
        }
        currentAccountIndex = selectedIndex;
    }

    public void performDeposit() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

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
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

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
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.println("Current balance: $" + selectedAccount.getBalance());
    }

    public void viewTransactionHistory() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.println("\nTransaction History:");
        System.out.println(selectedAccount.getTransactionHistoryText());
    }

    public void createAdditionalAccount() {
        bank.createAdditionalAccountForUser(currentUsername);
        System.out.println("New account created successfully.");
        System.out.println("Total accounts: " + getUserAccountIndexes().size());
    }

    public void closeAccount() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        selectedAccount.closeAccount();
        System.out.println("Account closed.");
    }

    public void performTransfer() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.println("Which account would you like to transfer money to?");
        java.util.List<Integer> indexes = getUserAccountIndexes();
        if (indexes.size() <= 1) {
            System.out.println("No other accounts available.");
            return;
        }
        int targetIndex = getAccountSelection(indexes, false);
        if (targetIndex < 0 || targetIndex == currentAccountIndex) {
            System.out.println("Invalid destination account.");
            return;
        }
        BankAccount accountToTransferTo = bank.getAccount(targetIndex);

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
        if (accountIndex < 0) {
            return;
        }

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
        if (accountIndex < 0) {
            return;
        }

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
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        boolean isOpen = selectedAccount.getStatus();
        System.out.println("Account status: " + (isOpen ? "Open" : "Closed"));
    }

    public void reopenAccount() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        boolean isOpen = selectedAccount.getStatus();

        if (isOpen) {
            System.out.println("Account is already open.");
            return;
        }

        selectedAccount.reopenAccount();
        System.out.println("Account reopened successfully.");
    }

    public void renameAccount() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.print("Enter your new account name: ");
        String newName = keyboardInput.nextLine();
        selectedAccount.rename(newName);
    }

    public void viewAllAccounts() {
        System.out.println("\nAll Accounts Summary:");
        System.out.println(bank.getAllAccountsSummary());
    }
}
