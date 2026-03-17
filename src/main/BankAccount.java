package main;

public class BankAccount {

    private double balance;
    private boolean active;

    public BankAccount() {
        this.balance = 0;
        this.active = true;
    }

    public void deposit(double amount) {
        if(amount > 0 && active) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void closeAccount() {
        this.active = false;
    }

    public boolean getAccountStatus() {
        return this.active;
    }

    public double getBalance() {
        return this.balance;
    }
}
