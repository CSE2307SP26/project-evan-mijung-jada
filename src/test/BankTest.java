package test;

import main.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BankTest {

    @Test
    public void testBankStartsWithNoAccounts() {
        Bank bank = new Bank();
        assertEquals(0, bank.getNumberOfAccounts());
    }

    @Test
    public void testCreateAdditionalAccount() {
        Bank bank = new Bank();
        bank.createAdditionalAccount(null);
        assertEquals(1, bank.getNumberOfAccounts());
        assertEquals("Account 1", bank.getAccount(0).getName());
        bank.createAdditionalAccount();
        assertEquals(2, bank.getNumberOfAccounts());
        assertEquals("Account 2", bank.getAccount(1).getName());
    }

    @Test
    public void testCreateMultipleAdditionalAccounts() {
        Bank bank = new Bank();
        bank.createAdditionalAccount(null);
        bank.createAdditionalAccount(null);
        assertEquals(2, bank.getNumberOfAccounts());
        assertEquals("Account 2", bank.getAccount(1).getName());
        bank.createAdditionalAccount();
        bank.createAdditionalAccount();
        assertEquals(4, bank.getNumberOfAccounts());
        assertEquals("Account 4", bank.getAccount(3).getName());
    }

    @Test
    public void testAddInterestPaymentUpdatesBalance() {
        Bank bank = new Bank();
        bank.createAdditionalAccount();
        bank.addInterestPayment(0, 7.25);
        assertEquals(7.25, bank.getAccount(0).getBalance(), 0.01);
    }

    @Test
    public void testCollectFeeUpdatesBalance() {
        Bank bank = new Bank();
        bank.createAdditionalAccount();
        bank.getAccount(0).deposit(10);
        bank.collectFee(0, 4.50);
        assertEquals(5.50, bank.getAccount(0).getBalance(), 0.01);
    }

    @Test
    public void testCreateCheckingAccount() {
        Bank bank = new Bank();
        bank.createAdditionalAccount("checking");
        assertEquals(1, bank.getNumberOfAccounts());
        assertEquals("Checking", bank.getAccount(0).getType());
    }
    
    @Test
    public void testCreateUserProfileIncreasesCount() {
        Bank bank = new Bank();
        bank.createUserProfile("jada", "1234");
        assertEquals(1, bank.getNumberOfUserProfiles());
    }

    @Test
    public void testAuthenticateUserWithValidCredentials() {
        Bank bank = new Bank();
        bank.createUserProfile("jada", "1234");
        assertEquals(true, bank.authenticateUser("jada", "1234"));
    }

    @Test
    public void testAuthenticateUserRejectsBadPin() {
        Bank bank = new Bank();
        bank.createUserProfile("jada", "1234");
        assertEquals(false, bank.authenticateUser("jada", "0000"));
    }
}