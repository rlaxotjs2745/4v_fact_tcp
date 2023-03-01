package com.example.shinhandata;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DbConnection {

    public Connection connection;
    @Bean
    public Connection getConnection(){
        //Connection connect = null;
////            dev
/*            String user = "c##fact_usr";
            String pw = "pass";
            String url = "jdbc:oracle:thin:@192.168.0.97:1521:orcl";*/

//          상주
/*            String user = "fact_user";
            String pw = "fact1230";
            String url = "jdbc:tibero:thin:@192.168.50.13:8629:SFINNOV";*/

////            김제
//            String user = "fact_user";
//            String pw = "fact1230";
//            String url = "jdbc:tibero:thin:@192.168.51.13:8629:SFINNOV";
        HikariConfig hikariConfig = new HikariConfig();
//local
/*        String url = "jdbc:oracle:thin:@192.168.0.97:1521:orcl";
        String username = "c##fact_usr";
        String password= "pass";*/
//sangju
        String url = "jdbc:tibero:thin:@192.168.50.13:8629:SFINNOV";
        String username = "fact_user";
        String password= "fact1230";

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(20);

        DataSource dataSource = new HikariDataSource(hikariConfig);

        try{
            connection = dataSource.getConnection();
        } catch (SQLException e){
            log.error("드라이버 실패 : " + e.toString());
        }
        return connection;
    }
}
