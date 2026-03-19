package main;

public class BankAccount {

    private double balance;
    private boolean open;

    public BankAccount() {
        this.balance = 0;
        this.open = true;
    }

    public void deposit(double amount) {
        if(amount > 0 && open) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void closeAccount() {
        this.open = false;
    }

    public boolean getStatus() {
        return this.open;
    }

    public double getBalance() {
        return this.balance;
    }
}
