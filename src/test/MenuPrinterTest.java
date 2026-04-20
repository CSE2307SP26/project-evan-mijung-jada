package test;

import main.Bank;
import main.MenuPrinter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class MenuPrinterTest {

    @Test
    public void testDisplayOptionsShowsInterestPayment() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            MenuPrinter printer = new MenuPrinter();
            printer.displayAdminOptions();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Add interest payment"));
    }

    @Test
    public void testDisplayOptionsShowsFeeCollection() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            MenuPrinter printer = new MenuPrinter();
            printer.displayAdminOptions();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Collect fee"));
    }

    @Test
    public void testDisplayLoginOptionsShowsCreateProfile() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            MenuPrinter printer = new MenuPrinter();
            printer.displayLoginOptions();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Create a user profile"));
    }

    @Test
    public void testDisplayAccountSelectionShowsNames() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            Bank bank = new Bank();
            bank.createAdditionalAccount(null);
            MenuPrinter printer = new MenuPrinter();
            printer.displayAccountSelection(bank);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Account 1"));
    }
}
