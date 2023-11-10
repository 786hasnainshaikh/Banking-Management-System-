package Accounts;

import DaoCoonection.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class accounts {
   // this class manages account details such as create new account and save in accounts table in database etc
    static Connection con= connection.getConnection();
    static Scanner sc=new Scanner(System.in);


    //Open New Account

    public static long openNewAccount(String email){
        // !isAccountExist(email) return false means there is no account with email so we will create new one
        if (!isAccountExist(email)){

            try{
                String query="insert into accounts (account_Number, FullName, Email, balance, security_Pin) values(?, ?, ?, ?, ?)";

                System.out.print("Enter Full Name: ");
                String fullName=sc.nextLine();
                System.out.print("Enter Initial Balance: ");
                double initialBalance=sc.nextDouble();
                System.out.print("Set your pin code:");
                int pinCode=sc.nextInt();

                PreparedStatement pstm= con.prepareStatement(query);
                long account_number=generateAccountNumber();

                // setting values in database
                pstm.setLong(1, account_number);
                pstm.setString(2, fullName);
                pstm.setString(3,email);
                pstm.setDouble(4,initialBalance);
                pstm.setInt(5, pinCode);

                // executing the query
                int i = pstm.executeUpdate();

                // check value stored into database or not
                if(i>0){
                    System.out.println("Account Created Successfully");
                    return account_number;
                } else {
                    System.out.println("Error!!! Account not created!!!");
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        } throw new RuntimeException("Account Already Exist!!!!");

    }


   //get account number
    public static long getAccountNumber(String email)  {

            String query="select account_Number from accounts where Email=?";
            try {
                PreparedStatement pstm= con.prepareStatement(query);
                pstm.setString(1, email);
                ResultSet resultSet = pstm.executeQuery();
                if (resultSet.next()){
                    long Account_numner= resultSet.getLong("account_Number");
                    return Account_numner;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

         throw new RuntimeException("Account Number doesn't exist with corresponding email");
    }

   // generate account number it will add 1 in existing last account number in database
    public static Long generateAccountNumber(){
        // this query provide latest account number in database
        String query="select account_Number from accounts order by account_Number desc limit 1";
        try{
            PreparedStatement pstm= con.prepareStatement(query);
            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()){
                // getting new account number by adding 1 in existing latest account number and return that account number
                long new_Account_Number = resultSet.getLong("account_Number");
                return new_Account_Number+1;
            }else {
                //if accounts table is empty it will return default value of  "10000100" as first account number
                return 10000100L;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return 10000100L;
    }



    // it will check if account Number exist in accounts table it return true
    public static Boolean isAccountExist(String email){
        String query="select account_Number from accounts where Email=?";
        try{
            PreparedStatement pstm=con.prepareStatement(query);
            pstm.setString(1,email);
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

}
