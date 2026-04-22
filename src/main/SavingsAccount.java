package main;

public class SavingsAccount extends BankAccount {
    private int transactionLimit;
    private int transactions;

    public SavingsAccount(String name) {
        super(name, "Savings");
        this.transactionLimit = 6;
        this.transactions = 0;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        if (!getStatus()) {
            throw new IllegalArgumentException("Account is closed.");
        }
        if (transactions >= transactionLimit) {
            throw new IllegalArgumentException("Reached Transaction Limit.");
        }

        this.setBalance(this.getBalance() + amount);
        addTransaction(
            String.format("Deposited $%.2f | New balance: $%.2f", amount, getBalance())
        );
        transactions++;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0.");
        }
        if (!getStatus()) {
            throw new IllegalArgumentException("Account is closed.");
        }
        if (transactions >= transactionLimit) {
            throw new IllegalArgumentException("Reached Transaction Limit.");
        }
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        this.setBalance(this.getBalance() - amount);
        addTransaction(
            String.format("Withdrew $%.2f | New balance: $%.2f", amount, getBalance())
        );
        transactions++;
        return true;
    }


    @Override
    public void transferMoney(BankAccount otherAccount, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than 0.");
        }
        if (!getStatus()) {
            throw new IllegalArgumentException("Account is closed.");
        }
        if (transactions >= transactionLimit) {
            throw new IllegalArgumentException("Reached Transaction Limit.");
        }
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        if (otherAccount == null) {
            throw new IllegalArgumentException("Invalid target account");
        }

        this.setBalance(this.getBalance() - amount);
        otherAccount.setBalance(otherAccount.getBalance() + amount);
        addTransaction(
            String.format("Transferred $%.2f | New balance: $%.2f", amount, getBalance())
        );

        otherAccount.addTransaction(
            String.format("$%.2f transferred to this account | New balance: $%.2f",
                amount, otherAccount.getBalance())
        );
        transactions++;
    }
}
