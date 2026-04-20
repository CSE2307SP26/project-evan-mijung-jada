package main;

public class MenuController {
    private static final int EXIT_SELECTION = 12;
    private static final int MAX_SELECTION = 16;

    private final MenuPrinter printer;
    private final AccountSelector accountSelector;
    private final CustomerMenu customerMenu;
    private final AdminMenu adminMenu;

    public MenuController(Bank bank, java.util.Scanner keyboardInput, MenuPrinter printer) {
        this.printer = printer;
        this.accountSelector = new AccountSelector(bank, keyboardInput, printer);
        this.customerMenu = new CustomerMenu(bank, keyboardInput, accountSelector);
        this.adminMenu = new AdminMenu(bank, keyboardInput, accountSelector);
    }

    public void run() {
        int selection = -1;
        while (selection != EXIT_SELECTION) {
            printer.displayOptions(adminMenu.isAdminMode());
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
            System.out.println();
        }
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = accountSelector.readInt();
        }
        return selection;
    }

    public int getAccountSelection() {
        return accountSelector.selectAccount();
    }

    public void processInput(int selection) {
        if (selection == EXIT_SELECTION) {
            System.out.println("Exiting app...");
            return;
        }

        if (customerMenu.handleSelection(selection)) {
            return;
        }

        if (adminMenu.handleSelection(selection)) {
            return;
        }

        System.out.println("Invalid selection.");
    }
}
