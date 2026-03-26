package test;

import main.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void testBankStartsWithOneAccount() {
        Bank bank = new Bank();
        assertEquals(1, bank.getNumberOfAccounts());
    }

    @Test
    public void testCreateAdditionalAccount() {
        Bank bank = new Bank();
        bank.createAdditionalAccount();
        assertEquals(2, bank.getNumberOfAccounts());
    }

    @Test
    public void testCreateMultipleAdditionalAccounts() {
        Bank bank = new Bank();
        bank.createAdditionalAccount();
        bank.createAdditionalAccount();
        assertEquals(3, bank.getNumberOfAccounts());
    }
}