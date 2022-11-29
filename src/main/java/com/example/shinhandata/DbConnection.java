package com.example.shinhandata;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DbConnection {
    public static Connection connection;

    public static Connection getConnection() {
        Connection connect = null;
////            dev
/*            String user = "c##fact_usr";
            String pw = "pass";
            String url = "jdbc:oracle:thin:@192.168.0.97:1521:orcl";*/

//          상주
            String user = "fact_user";
            String pw = "fact1230";
            String url = "jdbc:tibero:thin:@192.168.50.13:8629:SFINNOV";

////            김제
//            String user = "fact_user";
//            String pw = "fact1230";
//            String url = "jdbc:tibero:thin:@192.168.51.13:8629:SFINNOV";
        try{
//            Class.forName("oracle.jdbc.driver.OracleDriver");
            Class.forName("com.tmax.tibero.jdbc.TbDriver");

            connect = DriverManager.getConnection(url, user, pw);
        } catch (ClassNotFoundException e){
            log.error("드라이버 실패 : " + e.toString());
        } catch (SQLException e){
            log.error("DB 연결 실패 : " + e.toString());
        }
        return connect;
    }
}
