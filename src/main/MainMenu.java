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
        controller.runLoginLoop();
    }


    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}