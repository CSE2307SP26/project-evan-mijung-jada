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
            printer.displayOptions();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Add interest payment"));
    }

    @Test
    public void testDisplayAccountSelectionShowsNames() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        try {
            Bank bank = new Bank();
            bank.createAdditionalAccount();
            MenuPrinter printer = new MenuPrinter();
            printer.displayAccountSelection(bank);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Account 1"));
        assertTrue(output.contains("Account 2"));
    }
}
