package test;

import main.BankAccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount();
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testTransactionHistoryStartsEmpty() {
        BankAccount testAccount = new BankAccount();
        assertEquals("No transactions found.", testAccount.getTransactionHistoryText());
    }

    @Test
    public void testDepositAddsTransactionHistory() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);

        assertEquals(1, testAccount.getTransactionHistory().size());
        assertTrue(testAccount.getTransactionHistory().get(0).contains("Deposited $50.00"));
    }

    @Test
    public void testMultipleDepositsAddMultipleTransactions() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.deposit(25);

        assertEquals(2, testAccount.getTransactionHistory().size());
    }

    @Test
    public void testCloseExistingAccount() {
        BankAccount testAccount = new BankAccount();
        testAccount.closeAccount();
        assertFalse(testAccount.getStatus());
    }
}