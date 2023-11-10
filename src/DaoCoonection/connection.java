package DaoCoonection;

import java.sql.Connection;
import java.sql.DriverManager;

public class connection {

    public static Connection con;
    public static Connection getConnection () {
        try {
            final String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3307/bankingsystem";
            final String user_name = "root";
            final String user_password = "admin";
            Class.forName(driver);  // loaded the driver
            con = DriverManager.getConnection(url, user_name, user_password); // get the connection
        } catch (Exception e) {
            System.out.println(e);
        }

        return con;
    }
}
