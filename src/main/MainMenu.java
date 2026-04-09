package main;

public class MainMenu {

    private final MenuPrinter printer;
    private final MenuController controller;

    public MainMenu() {
        this.printer = new MenuPrinter();
        this.controller = new MenuController(new Bank(), new java.util.Scanner(System.in), printer);
    }

    public void displayOptions() {
        printer.displayOptions();
    }

    public void run() {
        controller.run();
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}