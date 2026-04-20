package main;

import java.util.Scanner;

public class MainMenu {

    private final MenuPrinter printer;
    private final MenuController controller;

    public MainMenu() {
        this.printer = new MenuPrinter();
        this.controller = new MenuController(new Bank(), new Scanner(System.in), printer);
    }

    public void displayOptions() {
        printer.displayLoginOptions();
    }

    public void run() {
        boolean keepRunning = true;
        while (keepRunning) {
            printer.displayLoginOptions();
            int selection = controller.getUserSelection(4);
            keepRunning = handleLoginSelection(selection);
        }
    }

    private boolean handleLoginSelection(int selection) {
        switch (selection) {
            case 1:
                controller.createUserProfile();
                return true;
            case 2:
                handleCustomerLogin();
                return true;
            case 3:
                handleAdminLogin();
                return true;
            case 4:
                System.out.println("Exiting app...");
                return false;
            default:
                System.out.println("Invalid selection.");
                return true;
        }
    }

    private void handleCustomerLogin() {
        String username = controller.promptLine("Enter username: ");
        String pin = controller.promptLine("Enter PIN: ");
        if (!controller.isUsernameTaken(username)) {
            System.out.println("Username not found.");
            return;
        }

        if (!controller.authenticateUser(username, pin)) {
            System.out.println("Wrong PIN.");
            return;
        }
        if (!controller.setCurrentUser(username)) {
            System.out.println("No accounts found for this user.");
            return;
        }

        controller.run(false);
    }

    private void handleAdminLogin() {
        String passcode = controller.promptLine("Enter admin passcode: ");
        if (controller.authenticateAdmin(passcode)) {
            controller.run(true);
        } else {
            System.out.println("Invalid passcode.");
        }
    }


    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}