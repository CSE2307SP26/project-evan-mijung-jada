package main;

public class CheckingAccount extends BankAccount {

    public CheckingAccount(String name) {
        super(name, "Checking");
    }

    @Override
    public void addInterestPayment(double amount) {
        // Checking accounts do not earn interest, so this method does nothing.
    }

}
