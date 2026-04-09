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
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount(null);
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testTransactionHistoryStartsEmpty() {
        BankAccount testAccount = new BankAccount(null);
        assertEquals("No transactions found.", testAccount.getTransactionHistoryText());
    }

    @Test
    public void testDepositAddsTransactionHistory() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(50);

        assertEquals(1, testAccount.getTransactionHistory().size());
        assertTrue(testAccount.getTransactionHistory().get(0).contains("Deposited $50.00"));
    }

    @Test
    public void testMultipleDepositsAddMultipleTransactions() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(50);
        testAccount.deposit(25);

        assertEquals(2, testAccount.getTransactionHistory().size());
    }

    @Test
    public void testCloseExistingAccount() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.closeAccount();
        assertFalse(testAccount.getStatus());
    }
    
    @Test
    public void testAccountStartsOpen() {
         BankAccount testAccount = new BankAccount();
         assertTrue(testAccount.getStatus());
    }

    @Test
    public void testTransferMoneyEmptyAccounts() {
        BankAccount testAccount1 = new BankAccount(null);
        BankAccount testAccount2 = new BankAccount(null);
        try {
            testAccount1.transferMoney(testAccount2, 50);
            fail();
        } catch (IllegalArgumentException e) {
            //test passes
        }  
    }
    
    @Test
    public void testTransferMoney() {
        BankAccount testAccount1 = new BankAccount(null);
        BankAccount testAccount2 = new BankAccount(null);
        testAccount1.deposit(50);
        testAccount2.deposit(25);
        testAccount1.transferMoney(testAccount2, 30);
        assertEquals(20, testAccount1.getBalance(), 0.01);
        assertEquals(55, testAccount2.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawReducesBalance() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(100);

        assertTrue(testAccount.withdraw(40));
        assertEquals(60, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawInvalidAmount() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(100);

        try {
            testAccount.withdraw(0);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        BankAccount testAccount = new BankAccount(null);

        try {
            testAccount.withdraw(10);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testCheckBalance() {
        BankAccount testAccount = new BankAccount(null);
        assertEquals(0, testAccount.getBalance(), 0.01);

        testAccount.deposit(75.25);
        assertEquals(75.25, testAccount.getBalance(), 0.01);

        testAccount.withdraw(25.25);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testRenameAccount() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.rename("my first account");
        assertEquals("my first account", testAccount.getName());
    }

    @Test
    public void testInterestPaymentIncreasesBalance() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.addInterestPayment(12.50);
        assertEquals(12.50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInterestPaymentAddsTransactionHistory() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.addInterestPayment(5);

        assertEquals(1, testAccount.getTransactionHistory().size());
        assertTrue(testAccount.getTransactionHistory().get(0).contains("Interest payment $5.00"));
    }

    @Test
    public void testInterestPaymentInvalidAmount() {
        BankAccount testAccount = new BankAccount(null);
        try {
            testAccount.addInterestPayment(0);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testInterestPaymentClosedAccount() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.closeAccount();

        try {
            testAccount.addInterestPayment(5);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testCollectFeeReducesBalance() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(20);

        testAccount.collectFee(5);

        assertEquals(15, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testCollectFeeAddsTransactionHistory() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(20);
        testAccount.collectFee(2.50);

        assertTrue(testAccount.getTransactionHistory().get(1).contains("Fee charged $2.50"));
    }

    @Test
    public void testCollectFeeInvalidAmount() {
        BankAccount testAccount = new BankAccount(null);
        try {
            testAccount.collectFee(0);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }

    @Test
    public void testCollectFeeInsufficientFunds() {
        BankAccount testAccount = new BankAccount(null);
        testAccount.deposit(3);

        try {
            testAccount.collectFee(5);
            fail();
        } catch (IllegalArgumentException e) {
            // test passes
        }
    }
    @Test
    public void testWithdrawValidAmount() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        account.withdraw(40);

        assertEquals(60, account.getBalance(), 0.01);
    }

    @Test
    public void testWithdrawExactBalance() {
        BankAccount account = new BankAccount();
        account.deposit(100);

        account.withdraw(100);

        assertEquals(0, account.getBalance(), 0.01);
    }

@Test
public void testWithdrawTooMuch() {
    BankAccount account = new BankAccount();
    account.deposit(50);

    try {
        account.withdraw(100);
        fail();
    } catch (IllegalArgumentException e) {
        // passes
    }
}

@Test
public void testWithdrawNegativeAmount() {
    BankAccount account = new BankAccount();

    try {
        account.withdraw(-10);
        fail();
    } catch (IllegalArgumentException e) {
        // passes
    }
}

@Test
public void testWithdrawFromClosedAccount() {
    BankAccount account = new BankAccount();
    account.deposit(50);
    account.closeAccount();

    try {
        account.withdraw(10);
        fail();
    } catch (IllegalArgumentException e) {
        // passes
    }
}

  @Test
  public void testWithdrawAddsTransactionHistory() {
      BankAccount account = new BankAccount();
      account.deposit(100);

      account.withdraw(25);

      assertEquals(2, account.getTransactionHistory().size());
      assertTrue(account.getTransactionHistory().get(1).contains("Withdrew $25.00"));
  }
  @Test
  public void testReopenClosedAccount() {
      BankAccount testAccount = new BankAccount();
      testAccount.closeAccount();
      testAccount.reopenAccount();
      assertTrue(testAccount.getStatus());
  }
}