/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author wvega
 */

public class MyConnections {
    public static Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CertWare","root","");
            System.out.println("Connection successful!");
        }
        catch(ClassNotFoundException e){
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
        catch(SQLException e){
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
        return conn;
    }
}
