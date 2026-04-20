package test;

import main.Bank;
import main.MenuController;
import main.MenuPrinter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class MenuControllerTest {

    @Test
    public void testGetUserSelectionReturnsChoice() {
        MenuController controller = new MenuController(new Bank(), new Scanner("8\n"), new MenuPrinter());
        assertEquals(8, controller.getUserSelection(13));
        assertEquals(8, controller.getUserSelection(16));
    }

    @Test
    public void testGetAccountSelectionReturnsIndex() {
        MenuController controller = new MenuController(new Bank(), new Scanner("1\n"), new MenuPrinter());
        assertEquals(0, controller.getAccountSelection());
    }
}
