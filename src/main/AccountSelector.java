package main;

import java.util.Scanner;

public class AccountSelector {

    private final Bank bank;
    private final Scanner keyboardInput;
    private final MenuPrinter printer;

    public AccountSelector(Bank bank, Scanner keyboardInput, MenuPrinter printer) {
        this.bank = bank;
        this.keyboardInput = keyboardInput;
        this.printer = printer;
    }

    public int selectAccount() {
        int max = bank.getNumberOfAccounts();
        printer.displayAccountSelection(bank);

        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Choose account number: ");
            selection = readInt();
        }

        return selection - 1;
    }

    public int readInt() {
        while (true) {
            String line = keyboardInput.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public double readDouble() {
        while (true) {
            String line = keyboardInput.nextLine();
            try {
                return Double.parseDouble(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
