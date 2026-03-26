public class Main {
    public static void main(String[] args) {
        Account account = new Account("12345", 500.00);

        System.out.println("Starting balance: $" + account.getBalance());

        account.withdraw(100.00);
        System.out.println("Balance after withdrawing $100: $" + account.getBalance());

        account.withdraw(1000.00);
        System.out.println("Balance after failed withdrawal: $" + account.getBalance());
    }
}