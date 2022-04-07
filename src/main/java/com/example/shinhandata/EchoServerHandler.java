package com.example.shinhandata;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg){
        boolean splitBool = false;
        String message = (String)msg;
//        System.out.println(message);
        if(message.contains("\\n")){
            splitBool = true;
            message = message.replaceAll("\\\\n", ""); //clrf처리, 오류 시 \두개 뺄 것
        }
        if(message.contains("\\r")){
            splitBool = true;
            message = message.replaceAll("\\\\r", "");// clrf처리, 오류 시 \두개 뺄 것
        }

        Channel channel = context.channel();
        String date = LocalDateTime.now().toString().replace("T", " ").substring(0,19);
        String query = "INSERT INTO SHINHAN_DATA ( IDX_SHINHANDATA, REC_DATA, REG_DT) VALUES ( IDX_SHINHANDATA_SEQ.NEXTVAL, ?, TO_DATE(?,'YYYY-MM-DD hh24:mi:ss'))";
        int startNum = 0;
//        ArrayList<String> messageArr = new ArrayList<>();
//        for(int i = 0; i < message.length(); i++){
////            System.out.println(message.substring(i, i+1));
//            if(i < message.length() - 4 && message.substring(i, i + 3).equals("#@S")){
//                startNum = i;
//            }
//            if(i > 3 && message.substring(i - 1, i+1).equals("$!")){
//                messageArr.add(message.substring(startNum, i + 1));
//                splitBool = true;
//            }
//        }
        ArrayList<String> keys = new ArrayList<>();
        ArrayList values = new ArrayList();
        String envQuery = "INSERT INTO TB_ENV_DATA ( IDX_TB_ENV_DATA, ?) VALUES ( TB_ENV_DATA_SEQ.NEXTVAL, ?)";
        String[] dataArr = message.split("\\|");
//        System.out.println(messageArr.size());
        for(int a = 1; a < dataArr.length - 1; a++){
            String[] dataObject = dataArr[a].split("=");
            if(dataObject.length == 2){
                keys.add(dataObject[0]);
                values.add(dataObject[1]);
            }
        }
//        System.out.println(envQuery);
//        query = query.substring(0, query.length()-1) + values.substring(0,values.length()-1) + ")";
//        System.out.println(query);
//        for(int i = 0; i < messageArr.size(); i++){
//            if(messageArr.get(i).length() > 0) {
//                try {
//                    conn = DbConnection.getConnection();
//                    pstm = conn.prepareStatement(query);
//                    pstm.setString(1, messageArr.get(i));
//                    pstm.setString(2, date);
//
//                    pstm.executeUpdate();
//                } catch (SQLException e) {
//                    System.out.println(e.toString());
//                }
//
//                if ("quit".equals(message)) {
//                    context.close();
//                }
//            }
//            channel.writeAndFlush("데이터 입력이 완료되었습니다." );
//        }
        try {
            conn = DbConnection.getConnection();
            System.out.println("1111111111");
            pstm = conn.prepareStatement(envQuery);
            System.out.println("2222222222222");
            pstm.setObject(1, keys);
            pstm.setObject(2, values);

            pstm.executeUpdate();
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }


}
