package com.example.shinhandata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection connection;

    public static Connection getConnection() {
        Connection connect = null;
        try{
            String user = "root";
            String pw = "1234";
            String url = "jdbc:oracle:thin:@14.63.174.153:1521:xe";

            Class.forName("oracle.jdbc.driver.OracleDriver");
            connect = DriverManager.getConnection(url, user, pw);
        } catch (ClassNotFoundException e){
            System.out.println("드라이버 실패 : " + e.toString());
        } catch (SQLException e){
            System.out.println("DB 연결 실패 : " + e.toString());
        }
        return connection;
    }
}
