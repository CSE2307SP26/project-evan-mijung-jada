package main;

import java.util.Scanner;

public class AdminMenu {

    private static final int ADMIN_MODE_SELECTION = 13;
    private static final int INTEREST_PAYMENT_SELECTION = 14;
    private static final int FEE_COLLECTION_SELECTION = 15;
    private static final int SET_WITHDRAWAL_LIMIT_SELECTION = 16;

    private final Bank bank;
    private final Scanner keyboardInput;
    private final AccountSelector accountSelector;
    private boolean adminMode;

    public AdminMenu(Bank bank, Scanner keyboardInput, AccountSelector accountSelector) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
        this.accountSelector = accountSelector;
        this.adminMode = false;
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public boolean handleSelection(int selection) {
        switch (selection) {
            case ADMIN_MODE_SELECTION:
                toggleAdminMode();
                return true;
            case INTEREST_PAYMENT_SELECTION:
                if (ensureAdminAccess()) {
                    addInterestPayment();
                }
                return true;
            case FEE_COLLECTION_SELECTION:
                if (ensureAdminAccess()) {
                    collectFee();
                }
                return true;
            case SET_WITHDRAWAL_LIMIT_SELECTION:
                if (ensureAdminAccess()) {
                    setWithdrawalLimit();
                }
                return true;
            default:
                return false;
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

    private void addInterestPayment() {
        int accountIndex = accountSelector.selectAccount();
        double interestAmount = readPositiveAmount("Enter interest payment amount: ");
        bank.addInterestPayment(accountIndex, interestAmount);
        System.out.println("Interest payment applied.");
    }

    private void collectFee() {
        int accountIndex = accountSelector.selectAccount();
        double feeAmount = readPositiveAmount("Enter fee amount: ");
        bank.collectFee(accountIndex, feeAmount);
        System.out.println("Fee collected.");
    }

    private void setWithdrawalLimit() {
        int accountIndex = accountSelector.selectAccount();
        double limit = readPositiveAmount("Enter withdrawal limit: ");
        bank.setWithdrawalLimit(accountIndex, limit);
        System.out.println("Withdrawal limit set successfully.");
    }

    private double readPositiveAmount(String prompt) {
        double amount = -1;
        while (amount <= 0) {
            System.out.print(prompt);
            amount = accountSelector.readDouble();
        }
        return amount;
    }
}