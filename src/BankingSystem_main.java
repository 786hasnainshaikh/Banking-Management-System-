import userr.*;
import Accounts.*;
import AccountManager.*;

import java.sql.SQLException;
import java.util.Scanner;


public class BankingSystem_main {
    public static void main(String[] args) throws SQLException {
        Scanner sc=new Scanner(System.in);

        String email;
        long accountNumber;

        while (true){
            main_menu();
            int choice=sc.nextInt();
            switch (choice){
                case (1):
                     // Register
                    user.Register();
                    break;


                case (2):
                    // login method return email of user we store that email in local variable
                    email = user.login();
                    // if email is valid
                    if(email!=null){
                        System.out.println("User logged in");

                    // there is 2 possibilities user having no account or user have a account

                        // if user is fresh having no account
                        if (!accounts.isAccountExist(email)){
                            System.out.println("1. open New Account");
                            System.out.println("2. Exit");
                            if(sc.nextInt()==1){
                                accountNumber = accounts.openNewAccount(email);
                                System.out.println("your Account number is: "+ accountNumber);
                            }
                            break;

                            // if user have already account now he can perform all operations
                        }else {
                         // getting particular account number for performing all transaction tasks on that account number and store in local variable
                            accountNumber= accounts.getAccountNumber(email);
                            int choice1=0;

                            while (choice1!=5){
                                System.out.println("1. WithDraw Money");
                                System.out.println("2. Deposit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");

                                System.out.print("Enter choice: ");
                                choice1= sc.nextInt();

                                switch (choice1){
                                    case (1):
                                        accountManager.withdrawMoney(accountNumber);
                                        break;

                                    case (2):
                                        accountManager.depositMoney(accountNumber);
                                        break;

                                    case (3):
                                        accountManager.transferMoney(accountNumber);
                                        break;

                                    case (4):
                                        accountManager.getBalance(accountNumber);
                                        break;

                                    case (5):
                                        choice1=5;
                                        break;


                                }

                            }
                        }
                    } else {
                        System.out.println("Invalid Email Address or Password!!!");
                    }
                    break;





                case (3):
                    // exit from system
                    System.out.print("Exiting System");
                    for (int i = 0; i < 5; i++) {
                        System.out.print(".");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println();
                    System.out.println("Thank u for Visiting Banking System");
                    System.exit(0);
                    break;

                case (4):
                    default:
                         System.out.println("Invalid Choice");
            }
        }

    }
    public static void main_menu(){
        System.out.println("---------- WELCOME TO BANKING SYSTEM -----------");
        System.out.println();
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }
}
