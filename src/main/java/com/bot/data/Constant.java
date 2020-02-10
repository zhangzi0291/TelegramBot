package com.bot.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Constant {

    @Value("${msgbot.token}")
    public void setMsgBotToken(String msgBotToken) {
        MSG_BOT_TOKEN = msgBotToken;
    }

    @Value("${msgbot.name}")
    public void setMsgBotName(String msgBotName) {
    MSG_BOT_NAME = msgBotName;
}

    @Value("${ownthink.appid}")
    public void setOwnthinkAppid(String ownthinkAppid) {
        OWNTHINK_APPID = ownthinkAppid;
    }

    @Value("${ownthink.userid}")
    public void setOwnthinkUserid(String ownthinkUserid) {
        OWNTHINK_USERID = ownthinkUserid;
    }

    @Value("${ownthink.userid}")
    public void setOwnthinkEnable(Boolean ownthinkEnable) {
        OWNTHINK_ENABLE = ownthinkEnable;
    }

    public static Map<String,String> oneToOne = new HashMap<>();

    public static Map<String,String> oneToMany = new HashMap<>();

    public static String MSG_BOT_TOKEN ;
    public static String MSG_BOT_NAME ;
    public static Boolean OWNTHINK_ENABLE ;
    public static String OWNTHINK_APPID ;
    public static String OWNTHINK_USERID ;

}
