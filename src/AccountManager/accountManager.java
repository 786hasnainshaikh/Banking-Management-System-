package AccountManager;

import DaoCoonection.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class accountManager {
    // this class perform operation on accounts table such as credit, debit , transfer etc
    static Connection con= connection.getConnection();
    static Scanner sc=new Scanner(System.in);



    // deposit money or credit money we will update that money in database
    public static void depositMoney(long accountNumber) throws SQLException {
        System.out.print("Enter the Amount: ");
        double amount= sc.nextDouble();
        System.out.print("Enter the Pin Code: ");
        String pinCode= sc.next();


        if (accountNumber!=0) {
            try {
                con.setAutoCommit(false);
                // first we have to on go particular row or entry
                String getParticularEntery="select * from accounts where account_Number=? and security_Pin=?";
                PreparedStatement pstm= con.prepareStatement(getParticularEntery);

                // setting values for arguments
                pstm.setLong(1,accountNumber);
                pstm.setString(2,pinCode);

                // executing the query and data will be saved in result set
                ResultSet resultSet = pstm.executeQuery();

                // if there is data in result we will update
                if (resultSet.next()){

                        try {
                            // here we update the database
                            String deposittQuery="update accounts set balance=balance+? where security_Pin=?";
                            PreparedStatement pstm1= con.prepareStatement(deposittQuery);
                            // setting values for arguments
                            pstm1.setDouble(1,amount);
                            pstm1.setString(2,pinCode);

                            // executing query and if table is affected or not
                            int i = pstm1.executeUpdate();
                            if(i>0){
                                System.out.println(" Rs: " +amount+ " Credit Successfully");
                                con.commit();
                                con.setAutoCommit(true);
                            }
                            else {
                                System.out.println("Transaction Failed");
                                con.rollback();
                                con.setAutoCommit(true);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                } else {
                    System.out.println("invalid pin code");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            con.setAutoCommit(true);
        }
    }


   // withdraw money and we will update that money in database
    public static void withdrawMoney(long accountNumber) throws SQLException {

        System.out.print("Enter the Amount: ");
        double amount= sc.nextDouble();
        System.out.print("Enter the Pin Code: ");
        String pinCode= sc.next();

        // first we have to on go particular row or entry
        if (accountNumber!=0) {

            try {
                con.setAutoCommit(false);
                String getParticularEntery="select * from accounts where account_Number=? and security_Pin=?";
                PreparedStatement pstm= con.prepareStatement(getParticularEntery);

                // setting values for arguments
                pstm.setLong(1,accountNumber);
                pstm.setString(2,pinCode);

                // executing the query and data will be saved in result set
                ResultSet resultSet = pstm.executeQuery();

                // if there is data in result we will update
                if (resultSet.next()){

                    // getting current balance from database
                    double CurrentBalance=resultSet.getDouble("balance");
                    if(amount<=CurrentBalance){

                        try {
                            // here we update the database
                            String withdrawQuery="update accounts set balance=balance-? where security_Pin=?";
                            PreparedStatement pstm1= con.prepareStatement(withdrawQuery);
                            // setting values for arguments
                            pstm1.setDouble(1,amount);
                            pstm1.setString(2,pinCode);

                            // executing query and if table is affected or not
                            int i = pstm1.executeUpdate();
                            if(i>0){
                                System.out.println(" Rs: " +amount+ " Debited Successfully");
                                con.commit();
                                con.setAutoCommit(true);
                            }
                            else {
                                System.out.println("Transaction Failed");
                                con.rollback();
                                con.setAutoCommit(true);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        System.out.println("Insufficient Amount");
                    }

                } else {
                    System.out.println("invalid pin code");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
             con.setAutoCommit(true);
        }
    }

    // transfer money

    public static void transferMoney(long sender_account_number ){

        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number=sc.nextLong();
        System.out.println("Enter the Amount");
        double amount=sc.nextDouble();
        System.out.print("Enter your pin code: ");
        String pin_code=sc.next();

        try{
            con.setAutoCommit(false);
            // check both accounts are valid
            if(sender_account_number!=0 && receiver_account_number!=0){
                String query="select * from accounts where account_Number=? and security_Pin =?";
                PreparedStatement pstm= con.prepareStatement(query);
                // setting values in arguments
                pstm.setLong(1,sender_account_number);
                pstm.setString(2, pin_code);

                ResultSet resultSet = pstm.executeQuery();
                if(resultSet.next()){
                    // fetch balance from sender account
                    double current_balance=resultSet.getDouble("balance");
                    // means transfer money is less than the balance in present in account
                    if(amount<=current_balance){
                        // transferring amount
                        try {
                            // this query is for sender account which send money
                            String withDrawQuery="update accounts set balance=balance-? where account_Number=?";
                            // this query is for receiver account which receive money
                            String depositQuery="update accounts set balance=balance+? where account_Number=?";

                            PreparedStatement withDrawStatement= con.prepareStatement(withDrawQuery);
                            PreparedStatement depositStatement=con.prepareStatement(depositQuery);

                            // setting values in withdraw query
                            withDrawStatement.setDouble(1, amount);
                            withDrawStatement.setLong(2,sender_account_number);

                            // setting values in deposit query
                            depositStatement.setDouble(1,amount);
                            depositStatement.setLong(2, receiver_account_number);

                            // executing the queries

                           int withdrawResult= withDrawStatement.executeUpdate();
                           int depositResult= depositStatement.executeUpdate();

                           if (withdrawResult>0 && depositResult>0){
                               System.out.println("Transaction Successful");
                               System.out.println("Rs:"+ amount+ " transfer Successfully");
                               con.commit();
                               con.setAutoCommit(true);
                           }else {
                               System.out.println("Transaction Failed!!!!");
                               con.rollback();
                               con.setAutoCommit(false);
                           }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("Insufficient Balance!!!");
                    }

                }else {
                    System.out.println("Invalid pin code!!!");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void getBalance( long accountNumber){

        System.out.print("Enter the pin code:");
        String pinCode=sc.next();

        try {
            String getBalancequery="select balance from accounts where account_Number=? and security_Pin=?";
            PreparedStatement pstm= con.prepareStatement(getBalancequery);
            // setting values for arguments
            pstm.setLong(1, accountNumber);
            pstm.setString(2,pinCode);

            // execute query and fetch data in result set
            ResultSet resultSet = pstm.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.print("Current balance is: " + balance);
                System.out.println();
            }
            else {
                System.out.println("Invalid!!!!!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
