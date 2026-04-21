package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuController {

    private enum FlowResult {
        CONTINUE_LOGIN,
        EXIT_APP,
        SWITCH_ACCOUNT
    }

    private static final int LOGIN_CREATE_PROFILE = 1;
    private static final int LOGIN_CUSTOMER = 2;
    private static final int LOGIN_ADMIN = 3;
    private static final int LOGIN_EXIT = 4;

    private static final int NO_ACCOUNT_CREATE_CHECKING = 1;
    private static final int NO_ACCOUNT_CREATE_SAVINGS = 2;
    private static final int NO_ACCOUNT_LOGOUT = 3;
    private static final int NO_ACCOUNT_EXIT = 4;

    private static final int CUSTOMER_DEPOSIT = 1;
    private static final int CUSTOMER_WITHDRAW = 2;
    private static final int CUSTOMER_CHECK_BALANCE = 3;
    private static final int CUSTOMER_VIEW_HISTORY = 4;
    private static final int CUSTOMER_TRANSFER = 5;
    private static final int CUSTOMER_RENAME = 6;
    private static final int CUSTOMER_CLOSE = 7;
    private static final int CUSTOMER_REOPEN = 8;
    private static final int CUSTOMER_STATUS = 9;
    private static final int CUSTOMER_OPEN_NEW = 10;
    private static final int CUSTOMER_VIEW_ALL = 11;
    private static final int CUSTOMER_SWITCH = 12;
    private static final int CUSTOMER_LOGOUT = 13;
    private static final int CUSTOMER_EXIT = 14;

    private static final int ADMIN_INTEREST = 1;
    private static final int ADMIN_FEE = 2;
    private static final int ADMIN_VIEW_ALL = 3;
    private static final int ADMIN_SEARCH = 4;
    private static final int ADMIN_EXIT = 5;

    private final Bank bank;
    private final Scanner keyboardInput;
    private final MenuPrinter printer;
    private String currentUsername;
    private int currentAccountIndex;

    public MenuController(Bank bank, Scanner keyboardInput, MenuPrinter printer) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
        this.printer = printer;
        this.currentUsername = null;
        this.currentAccountIndex = -1;
    }

    public void runLoginLoop() {
        boolean keepRunning = true;
        while (keepRunning) {
            printer.displayLoginOptions();
            int selection = readIntInRange(LOGIN_CREATE_PROFILE, LOGIN_EXIT);
            switch (selection) {
                case LOGIN_CREATE_PROFILE:
                    keepRunning = createUserProfileFlow();
                    break;
                case LOGIN_CUSTOMER:
                    keepRunning = handleCustomerLogin();
                    break;
                case LOGIN_ADMIN:
                    keepRunning = handleAdminLogin();
                    break;
                case LOGIN_EXIT:
                    System.out.println("Exiting app...");
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
                    break;
            }
        }
    }

    public int getUserSelection(int max) {
        return readIntInRange(1, max);
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
        createUserProfileFlow();
    }

    public String promptLine(String prompt) {
        System.out.print(prompt);
        return keyboardInput.nextLine();
    }

    private boolean createUserProfileFlow() {
        String username = null;
        while (true) {
            username = readNonBlank("Enter a username: ");
            String pin = readPin();
            try {
                bank.createUserProfile(username, pin);
                System.out.println("User profile created.");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        this.currentUsername = username;
        this.currentAccountIndex = -1;
        return runNoAccountFlow();
    }

    private boolean handleCustomerLogin() {
        String username = promptLine("Enter username: ");
        String pin = promptLine("Enter PIN: ");

        if (!isUsernameTaken(username)) {
            System.out.println("Username not found.");
            return true;
        }

        if (!authenticateUser(username, pin)) {
            System.out.println("Wrong PIN.");
            return true;
        }

        this.currentUsername = username.trim();
        this.currentAccountIndex = -1;

        List<Integer> indexes = bank.getAccountIndexesForUser(currentUsername);
        if (indexes.isEmpty()) {
            return runNoAccountFlow();
        }
        if (indexes.size() == 1) {
            currentAccountIndex = indexes.get(0);
            FlowResult result = runCustomerAccountLoop();
            return handleFlowResult(result);
        }

        return runAccountSelectionFlow();
    }

    private boolean handleAdminLogin() {
        String passcode = promptLine("Enter admin passcode: ");
        if (authenticateAdmin(passcode)) {
            return runAdminLoop();
        }
        System.out.println("Invalid passcode.");
        return true;
    }

    private boolean runNoAccountFlow() {
        while (true) {
            printer.displayNoAccountOptions(currentUsername);
            int selection = readIntInRange(NO_ACCOUNT_CREATE_CHECKING, NO_ACCOUNT_EXIT);
            switch (selection) {
                case NO_ACCOUNT_CREATE_CHECKING:
                    String checkingName = readNonBlank("Enter account name: ");
                    int newIndex = createAccountForCurrentUser("checking", checkingName);
                    if (newIndex >= 0) {
                        currentAccountIndex = newIndex;
                        FlowResult result = runCustomerAccountLoop();
                        return handleFlowResult(result);
                    }
                    break;
                case NO_ACCOUNT_CREATE_SAVINGS:
                    readNonBlank("Enter account name: ");
                    System.out.println("Savings accounts are not available yet.");
                    break;
                case NO_ACCOUNT_LOGOUT:
                    clearCurrentUser();
                    return true;
                case NO_ACCOUNT_EXIT:
                    System.out.println("Exiting app...");
                    return false;
                default:
                    System.out.println("Invalid selection.");
                    break;
            }
        }
    }

    private boolean runAccountSelectionFlow() {
        while (true) {
            List<Integer> indexes = bank.getAccountIndexesForUser(currentUsername);
            if (indexes.isEmpty()) {
                return runNoAccountFlow();
            }

            printer.displayAccountSelectionHeader(currentUsername);
            printer.displayAccountSelection(bank, indexes, false);
            int openNewOption = indexes.size() + 1;
            int logoutOption = indexes.size() + 2;
            System.out.println(openNewOption + ". Open a new account");
            System.out.println(logoutOption + ". Logout");

            int selection = readIntInRange(1, logoutOption);
            if (selection <= indexes.size()) {
                currentAccountIndex = indexes.get(selection - 1);
                FlowResult result = runCustomerAccountLoop();
                if (result == FlowResult.SWITCH_ACCOUNT) {
                    continue;
                }
                return handleFlowResult(result);
            }
            if (selection == openNewOption) {
                Integer newIndex = openNewAccountFlow();
                if (newIndex != null) {
                    currentAccountIndex = newIndex;
                    FlowResult result = runCustomerAccountLoop();
                    if (result == FlowResult.SWITCH_ACCOUNT) {
                        continue;
                    }
                    return handleFlowResult(result);
                }
                continue;
            }

            clearCurrentUser();
            return true;
        }
    }

    private FlowResult runCustomerAccountLoop() {
        if (!ensureCurrentAccount()) {
            return FlowResult.CONTINUE_LOGIN;
        }

        while (true) {
            BankAccount currentAccount = getCurrentAccount();
            printer.displayCustomerAccountMenu(currentUsername, currentAccount.getName());
            int selection = readIntInRange(CUSTOMER_DEPOSIT, CUSTOMER_EXIT);

            switch (selection) {
                case CUSTOMER_DEPOSIT:
                    performDeposit();
                    break;
                case CUSTOMER_WITHDRAW:
                    performWithdraw();
                    break;
                case CUSTOMER_CHECK_BALANCE:
                    checkBalance();
                    break;
                case CUSTOMER_VIEW_HISTORY:
                    viewTransactionHistory();
                    break;
                case CUSTOMER_TRANSFER:
                    performTransfer();
                    break;
                case CUSTOMER_RENAME:
                    renameAccount();
                    break;
                case CUSTOMER_CLOSE:
                    closeAccount();
                    break;
                case CUSTOMER_REOPEN:
                    reopenAccount();
                    break;
                case CUSTOMER_STATUS:
                    checkAccountStatus();
                    break;
                case CUSTOMER_OPEN_NEW:
                    Integer newIndex = openNewAccountFlow();
                    if (newIndex != null) {
                        currentAccountIndex = newIndex;
                    }
                    break;
                case CUSTOMER_VIEW_ALL:
                    viewAllAccounts();
                    break;
                case CUSTOMER_SWITCH:
                    return FlowResult.SWITCH_ACCOUNT;
                case CUSTOMER_LOGOUT:
                    clearCurrentUser();
                    return FlowResult.CONTINUE_LOGIN;
                case CUSTOMER_EXIT:
                    System.out.println("Exiting app...");
                    return FlowResult.EXIT_APP;
                default:
                    System.out.println("Invalid selection.");
                    break;
            }
            System.out.println();
        }
    }

    private boolean runAdminLoop() {
        while (true) {
            printer.displayAdminMenu();
            int selection = readIntInRange(ADMIN_INTEREST, ADMIN_EXIT);
            switch (selection) {
                case ADMIN_INTEREST:
                    addInterestPayment();
                    break;
                case ADMIN_FEE:
                    collectFee();
                    break;
                case ADMIN_VIEW_ALL:
                    System.out.println("\nAll Accounts Summary:");
                    System.out.println(bank.getAllAccountsSummary(true));
                    break;
                case ADMIN_SEARCH:
                    searchAccountsByUsername();
                    break;
                case ADMIN_EXIT:
                    System.out.println("Exiting admin mode...");
                    return true;
                default:
                    System.out.println("Invalid selection.");
                    break;
            }
            System.out.println();
        }
    }

    private boolean handleFlowResult(FlowResult result) {
        if (result == FlowResult.EXIT_APP) {
            return false;
        }
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

    private void clearCurrentUser() {
        currentUsername = null;
        currentAccountIndex = -1;
    }

    private Integer openNewAccountFlow() {
        System.out.println("1. Open checking account");
        System.out.println("2. Open savings account");
        System.out.println("3. Cancel");

        int selection = readIntInRange(1, 3);
        if (selection == 3) {
            return null;
        }

        if (selection == 2) {
            readNonBlank("Enter account name: ");
            System.out.println("Savings accounts are not available yet.");
            return null;
        }

        String checkingName = readNonBlank("Enter account name: ");
        int newIndex = createAccountForCurrentUser("checking", checkingName);
        if (newIndex < 0) {
            return null;
        }

        if (readYesNo("Switch to new account? (y/n): ")) {
            return newIndex;
        }
        return null;
    }

    private int createAccountForCurrentUser(String type, String accountName) {
        try {
            bank.createAccountForUser(currentUsername, type, accountName);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return -1;
        }

        List<Integer> indexes = bank.getAccountIndexesForUser(currentUsername);
        if (indexes.isEmpty()) {
            return -1;
        }
        return indexes.get(indexes.size() - 1);
    }

    private int getAccountSelection(List<Integer> indexes, boolean showOwner) {
        printer.displayAccountSelection(bank, indexes, showOwner);
        if (indexes.isEmpty()) {
            return -1;
        }

        int selection = readIntInRange(1, indexes.size());
        return indexes.get(selection - 1);
    }

    private int getAccountSelection(boolean showOwner) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < bank.getNumberOfAccounts(); i++) {
            indexes.add(i);
        }
        return getAccountSelection(indexes, showOwner);
    }

    private void performDeposit() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        double depositAmount = readPositiveAmount("How much would you like to deposit: ");
        try {
            selectedAccount.deposit(depositAmount);
            System.out.println("Deposit complete. New balance: $" + String.format("%.2f", selectedAccount.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    private void performWithdraw() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.println("Your current balance is " + selectedAccount.getBalance());
        double withdrawAmount = readPositiveAmount("Enter an amount: ");

        try {
            selectedAccount.withdraw(withdrawAmount);
            System.out.println("Withdrawal complete. New balance: $" + String.format("%.2f", selectedAccount.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    private void checkBalance() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.println("Current balance: $" + String.format("%.2f", selectedAccount.getBalance()));
    }

    private void viewTransactionHistory() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        System.out.println("\nTransaction History:");
        System.out.println(selectedAccount.getTransactionHistoryText());
    }

    private void performTransfer() {
        BankAccount fromAccount = getCurrentAccount();
        if (fromAccount == null) {
            return;
        }

        List<Integer> indexes = bank.getAccountIndexesForUser(currentUsername);
        if (indexes.size() <= 1) {
            System.out.println("No other accounts available.");
            return;
        }

        List<Integer> targetIndexes = new ArrayList<>();
        for (int index : indexes) {
            if (index != currentAccountIndex) {
                targetIndexes.add(index);
            }
        }

        System.out.println("Which account would you like to transfer money to?");
        int targetIndex = getAccountSelection(targetIndexes, false);
        if (targetIndex < 0) {
            return;
        }

        BankAccount accountToTransferTo = bank.getAccount(targetIndex);
        double transferAmount = readPositiveAmount("Enter an amount: ");

        try {
            fromAccount.transferMoney(accountToTransferTo, transferAmount);
            System.out.println("Transfer complete.");
        } catch (IllegalArgumentException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    private void renameAccount() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        String newName = readNonBlank("Enter your new account name: ");
        selectedAccount.rename(newName);
        System.out.println("Account renamed.");
    }

    private void closeAccount() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        if (!readYesNo("Are you sure you want to close this account? (y/n): ")) {
            return;
        }

        selectedAccount.closeAccount();
        System.out.println("Account closed.");
    }

    private void reopenAccount() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        if (selectedAccount.getStatus()) {
            System.out.println("Account is already open.");
            return;
        }

        selectedAccount.reopenAccount();
        System.out.println("Account reopened successfully.");
    }

    private void checkAccountStatus() {
        BankAccount selectedAccount = getCurrentAccount();
        if (selectedAccount == null) {
            return;
        }

        boolean isOpen = selectedAccount.getStatus();
        System.out.println("Account status: " + (isOpen ? "Open" : "Closed"));
    }

    private void viewAllAccounts() {
        System.out.println("\nAll Accounts Summary:");
        String summary = bank.getAccountsSummaryForUser(currentUsername);
        if (summary.trim().isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.println(summary);
    }

    private void addInterestPayment() {
        int accountIndex = getAccountSelection(true);
        if (accountIndex < 0) {
            return;
        }

        double interestAmount = readPositiveAmount("Enter interest payment amount: ");
        bank.addInterestPayment(accountIndex, interestAmount);
        System.out.println("Interest payment applied.");
    }

    private void collectFee() {
        int accountIndex = getAccountSelection(true);
        if (accountIndex < 0) {
            return;
        }

        double feeAmount = readPositiveAmount("Enter fee amount: ");
        bank.collectFee(accountIndex, feeAmount);
        System.out.println("Fee collected.");
    }

    private void searchAccountsByUsername() {
        String username = readNonBlank("Enter username: ");
        String summary = bank.getAccountsSummaryForUser(username);
        if (summary.trim().isEmpty()) {
            System.out.println("No accounts found for " + username + ".");
            return;
        }
        System.out.println("\nAccounts for " + username + ":");
        System.out.println(summary);
    }

    private String readNonBlank(String prompt) {
        String value = "";
        while (value.trim().isEmpty()) {
            System.out.print(prompt);
            value = keyboardInput.nextLine();
            if (value.trim().isEmpty()) {
                System.out.println("Input cannot be empty.");
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

    private int readIntInRange(int min, int max) {
        int value = min - 1;
        while (value < min || value > max) {
            System.out.print("Please make a selection: ");
            String line = keyboardInput.nextLine();
            try {
                value = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                value = min - 1;
            }
            if (value < min || value > max) {
                System.out.println("Invalid selection. Enter a number between " + min + " and " + max + ".");
            }
        }
        return value;
    }

    private double readPositiveAmount(String prompt) {
        double amount = -1;
        while (amount <= 0) {
            System.out.print(prompt);
            String line = keyboardInput.nextLine();
            try {
                amount = Double.parseDouble(line.trim());
            } catch (NumberFormatException e) {
                amount = -1;
            }
            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
            }
        }
        return amount;
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = keyboardInput.nextLine().trim().toLowerCase();
            if ("y".equals(value) || "yes".equals(value)) {
                return true;
            }
            if ("n".equals(value) || "no".equals(value)) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }
}
