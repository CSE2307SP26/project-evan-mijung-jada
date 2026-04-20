package test;

import main.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void testBankStartsWithOneAccount() {
        Bank bank = new Bank();
        assertEquals(1, bank.getNumberOfAccounts());
        assertEquals("Account 1", bank.getAccount(0).getName());
    }

    @Test
    public void testCreateAdditionalAccount() {
        Bank bank = new Bank();
        bank.createAdditionalAccount();
        assertEquals(2, bank.getNumberOfAccounts());
        assertEquals("Account 2", bank.getAccount(1).getName());
    }

    @Test
    public void testCreateMultipleAdditionalAccounts() {
        Bank bank = new Bank();
        bank.createAdditionalAccount();
        bank.createAdditionalAccount();
        assertEquals(3, bank.getNumberOfAccounts());
        assertEquals("Account 3", bank.getAccount(2).getName());
    }

    @Test
    public void testAddInterestPaymentUpdatesBalance() {
        Bank bank = new Bank();
        bank.addInterestPayment(0, 7.25);
        assertEquals(7.25, bank.getAccount(0).getBalance(), 0.01);
    }

    @Test
    public void testCollectFeeUpdatesBalance() {
        Bank bank = new Bank();
        bank.getAccount(0).deposit(10);
        bank.collectFee(0, 4.50);
        assertEquals(5.50, bank.getAccount(0).getBalance(), 0.01);
    }

    @Test
    public void testCreateUserProfileIncreasesCount() {
        Bank bank = new Bank();
        bank.createUserProfile("jada", "1234");
        assertEquals(1, bank.getNumberOfUserProfiles());
    }
}