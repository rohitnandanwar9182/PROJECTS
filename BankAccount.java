package REALPROJECTS;
class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
public class BankAccount {
    private int balance = 0;

    void deposit(int money) {
        balance = balance + money;
    }

    void withdraw(int money) throws InsufficientFundsException {
        if (money > balance) {
            throw new InsufficientFundsException("NOT ENOUGH MONEY");
        }
        balance = balance - money;
    }

    public int getBalance() {
        return balance;
    }

    public static void main(String[] args) { 
        
        BankAccount c1 = new BankAccount();
        c1.deposit(3600);
        System.out.println(+ c1.getBalance());

        //
         try {
            c1.withdraw(787);
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
      //  
      System.out.println(c1.balance);
  } 
}
