package com.example.shinhandata;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg){
        String message = (String)msg;
        Channel channel = context.channel();
        String query = "insert into shinhandata ( idx,";
        String values = ") values ( SHINHANDATA_SEQ.NEXTVAL";
        String[] dataArr = message.split("\\|");
        for(int a = 1; a < dataArr.length - 1; a++){
            String[] dataObject = dataArr[a].split("=");
            if(dataObject.length == 2){
                query += dataObject[0] + ",";
                values += dataObject[1] + ",";
            }
        }
        query = query.substring(0, query.length()-1) + values.substring(0,values.length()-1) + ")";

        System.out.println(query);

        try{
            conn = DbConnection.getConnection();
            pstm = conn.prepareStatement(query);
            int result = pstm.executeUpdate();
            channel.writeAndFlush("데이터 입력이 완료되었습니다. 해당 데이터의 번호는 " + result + " 입니다." + "\n");
        } catch (SQLException e) {
            System.out.println("예외발생");
        }

        if("quit".equals(message)){
            context.close();
        }

    }
}
