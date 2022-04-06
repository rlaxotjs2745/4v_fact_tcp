package com.example.shinhandata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection connection;

    public static Connection getConnection() {
        Connection connect = null;
        try{
//            String user = "c##fact_usr";
//            String pw = "pass";
//            String url = "jdbc:oracle:thin:@192.168.0.97:1521:orcl";

            String user = "fact_user";
            String pw = "fact1230";
            String url = "jdbc:tibero:thin:@192.168.50.13:8629:SFINNOV";

            Class.forName("oracle.jdbc.driver.OracleDriver");
            connect = DriverManager.getConnection(url, user, pw);
        } catch (ClassNotFoundException e){
            System.out.println("드라이버 실패 : " + e.toString());
        } catch (SQLException e){
            System.out.println("DB 연결 실패 : " + e.toString());
        }
        return connect;
    }
}
