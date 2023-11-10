package userr;

import DaoCoonection.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Stack;

public class user {

    // this class manages login details such as register, login and save data into user table in database
   static Connection con=connection.getConnection();
   static Scanner sc=new Scanner(System.in);

    public static void Register(){

        System.out.print("Enter the Full Name: ");
        String fullName= sc.nextLine();
        System.out.print("Enter the Email: ");
        String email=sc.nextLine();
        System.out.print("Enter the password:");
        String password=sc.nextLine();

        if(isUserExist(email)){
            System.out.println("User Already Exist");
            return;
        }
        else {
            String query="insert into user (FullName, Email, Password) values(?, ?, ?)";
            try{
                PreparedStatement pstm= con.prepareStatement(query);

                pstm.setString(1,fullName);
                pstm.setString(2,email);
                pstm.setString(3,password);

                int i = pstm.executeUpdate();

                if(i>0){
                    System.out.println("User Registered successfully");
                }else {
                    System.out.println("something went wrong");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    public static String login(){

        System.out.print("Enter email ID: ");
        String email=sc.nextLine();
        System.out.print("Enter password: ");
        String password=sc.nextLine();
        String query="select * from user where Email=? and Password=?";
        try {
            PreparedStatement pstm= con.prepareStatement(query);

            pstm.setString(1, email);
            pstm.setString(2,password);

            ResultSet resultSet = pstm.executeQuery();
            if(resultSet.next()){
                return email;
            }else {
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isUserExist(String email) {
        String queery="select * from user where email=?";
        try{
            PreparedStatement pstm= con.prepareStatement(queery);
            pstm.setString(1, email);
            ResultSet resultSet = pstm.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
