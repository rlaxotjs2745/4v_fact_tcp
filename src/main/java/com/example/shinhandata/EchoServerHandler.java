package com.example.shinhandata;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.sql.*;
import java.time.LocalDateTime;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg){
        String message = (String)msg;
//        message = message.replaceAll("\r","").replaceAll("\n",""); //crlf 필터링
//        System.out.println(message);
//        String[] messageArr = message.split("\\$!");
        Channel channel = context.channel();
        String[] a = LocalDateTime.now().toString().split("T");
        a[0] = a[0].replace("-", ".");
        a[1] = a[1].substring(0,5);
        String query = "INSERT INTO SHINHAN_DATA ( IDX_SHINHANDATA, REC_DATA, REG_DT)VALUES( IDX_SHINHANDATA_SEQ.NEXTVAL, ?, TO_DATE(?, YYYY.MM.DD hh24:mm:ss)";
//        String[] dataArr = message.split("\\|");
//        for(int a = 1; a < dataArr.length - 1; a++){
//            String[] dataObject = dataArr[a].split("=");
//            if(dataObject.length == 2){
//                query += dataObject[0] + ",";
//                values += dataObject[1] + ",";
//            }
//        }
//        query = query.substring(0, query.length()-1) + values.substring(0,values.length()-1) + ")";

//        System.out.println(query);

        try{
            conn = DbConnection.getConnection();
            pstm = conn.prepareStatement(query);
            pstm.setString(1, message);
            pstm.setDate(2, );

            int result = pstm.executeUpdate();
            channel.writeAndFlush("데이터 입력이 완료되었습니다. 해당 데이터의 번호는 " + result + " 입니다." );
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        if("quit".equals(message)){
            context.close();
        }

    }


}
