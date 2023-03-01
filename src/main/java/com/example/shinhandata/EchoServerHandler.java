package com.example.shinhandata;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;

@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    public EchoServerHandler(Connection _conn){
        conn = _conn;
    }
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg){
        boolean splitBool = false;
        //ByteBuf readMessage = (ByteBuf) msg;
        //String message = msg.toString(StandardCharsets.US_ASCII);
        String message = (String)msg;
        //System.out.println(message);
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
        ArrayList<String> messageArr = new ArrayList<>();
        for(int i = 0; i < message.length(); i++){
            if(i < message.length() - 4 && message.substring(i, i + 3).equals("#@S")){
                startNum = i;
            }
            if(i > 3 && message.substring(i - 1, i+1).equals("$!")){
                messageArr.add(message.substring(startNum, i + 1));
                splitBool = true;
            }
        }

        String keys = "";
        String values = "";
        for (int i = 0; i < messageArr.size(); i++) {
            keys = "";
            values = "";
            String[] dataArr = messageArr.get(i).split("\\|");
            for (int a = 1; a < dataArr.length - 1; a++) {
                String[] dataObject = dataArr[a].split("=");
                if (a == 1 || a == 2) {
                    keys += dataObject[0] + ", ";
                    values += "\'" + dataObject[1] + "\', ";
                } else if (dataObject.length == 2) {
                    keys += dataObject[0] + ", ";
                    values += dataObject[1] + ", ";

                }
            }
            keys = keys.substring(0, keys.length() - 2);
            values = values.substring(0, values.length() - 2);

            String newQuery = "INSERT INTO TB_ENV_DATA ( IDX_TB_ENV_DATA, " + keys + ") VALUES ( TB_ENV_DATA_SEQ.NEXTVAL, " + values + ")";

            try {
                //conn = DbConnection.getConnection();
                pstm = conn.prepareStatement(newQuery);
                pstm.executeUpdate();

                pstm = conn.prepareStatement(query);
                pstm.setString(1, messageArr.get(i));
                pstm.setString(2, date);
                pstm.executeUpdate();

            }catch (SQLException e) {
                log.error("SQLException:" + e.toString());
            }
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }

}
