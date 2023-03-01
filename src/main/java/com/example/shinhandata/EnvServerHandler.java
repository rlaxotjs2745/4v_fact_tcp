package com.example.shinhandata;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
public class EnvServerHandler extends ChannelInboundHandlerAdapter {

    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    public EnvServerHandler(Connection _conn){
        conn = _conn;
    }
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg){
        //boolean splitBool = false;

        String message = (String)msg;
        message+="$!";//델리미터 필터에서 제거해서 올라와서 다시 넣음

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
                //splitBool = true;
            }
        }

        String keys = "";
        String values = "";

        //for (int i = 0; i < 1; i++) {//하나만 넣자.. 쌓인거 다 넣으니까.. 문제가 되네.
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
                pstm = conn.prepareStatement(newQuery);
                pstm.executeUpdate();

                pstm = conn.prepareStatement(query);
                pstm.setString(1, messageArr.get(i));
                pstm.setString(2, date);
                pstm.executeUpdate();

                log.info("db input success!:"+ date + "   :"+ message.substring(message.length()-32,message.length()-20));

            }catch (SQLException e) {
                log.error("db input fail!:" + e.toString());
            }
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }

}
