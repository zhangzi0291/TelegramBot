package com.bot.bots.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bot.bots.service.IMsgBotService;
import com.bot.data.Constant;
import com.bot.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MsgBotServiceImpl implements IMsgBotService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<SendMessage> sendWelcome(Message message) throws Exception{
        List<SendMessage > resultList = new ArrayList<>();

        List<User> newMemberList = message.getNewChatMembers();

        //致欢迎词
        if(!CollectionUtils.isEmpty(newMemberList)){
            long finalChat_id = message.getChatId();
            newMemberList.forEach(user -> {
                //机器人不打招呼
                if(user.getBot()){
                    return;
                }
                String welcomeText = Constant.welcomeText.replaceAll("\\{name}",user.getFirstName());
                SendMessage sendMessage = new SendMessage()
                        .setChatId(finalChat_id)
                        .setText(welcomeText);

                resultList.add(sendMessage);
                log(user.getFirstName(),user.getId(),user.getUserName(),"newMembers",sendMessage.getText());
            });
        }
        return resultList;
    }

    @Override
    public SendMessage respondPrivateMessage(Message message) throws Exception{
        SendMessage sendMessage = new SendMessage();

        User user = message.getFrom();
        String text = message.getText();
        String user_first_name = message.getFrom().getFirstName();
        String user_last_name = message.getFrom().getLastName();
        String name = "@" + user_first_name + " " + user_last_name;
        String user_id = message.getFrom().getId() + "";

        StringBuffer answer = new StringBuffer();
        //在问题表中有的话使用问题表的回答，否则使用思知的回答

        if (Constant.messageMap.keySet().contains(text)) {
            answer.append(Constant.messageMap.get(text));

            sendMessage.setChatId(message.getChatId())
                    .setText(answer.toString());
        } else {
            sendMessage = generalAnswer(text,message.getChatId());
        }
        log(user.getFirstName(),user.getId(),user.getUserName(),text,sendMessage.getText());
        return sendMessage;
    }

    @Override
    public SendMessage respondGroupMessage(Message message) throws Exception{
        SendMessage sendMessage = new SendMessage();

        User user = message.getFrom();
        String text = message.getText();
        String user_first_name = message.getFrom().getFirstName();
        String user_last_name = message.getFrom().getLastName();
        String name = "@" + user_first_name + " " + user_last_name;
        String user_id = message.getFrom().getId() + "";

        StringBuffer answer = new StringBuffer();
        //在问题表中有的话使用问题表的回答，否则使用思知的回答
        if (Constant.messageMap.keySet().contains(text)) {
            answer.append(Constant.messageMap.get(text));

            sendMessage.setChatId(message.getChatId())
                    .setText(answer.toString());
        } else {
            sendMessage = generalAnswer(text,message.getChatId());
        }

        log(user.getFirstName(),user.getId(),user.getUserName(),"newMembers",sendMessage.getText());
        return sendMessage;
    }

    @Override
    public SendMessage respondChannelMessage(Message channelPost) throws Exception{
        SendMessage sendMessage = new SendMessage();

//        String text = channelPost.getText();
//        Long chat_id = channelPost.getChatId();
//
//        String user_username = channelPost.getChat().getUserName();
//        String user_id = channelPost.getChat().getId()+"";
//
//        String atName = "";
//        //获取消息中是否有@的第一个人
//        if (channelPost.getEntities() != null) {
//            if (channelPost.getEntities().size() > 0) {
//                MessageEntity entity = channelPost.getEntities().get(0);
//                atName = entity.getText();
//            }
//        }
//
//        StringBuffer answer = new StringBuffer();
//        if (Constant.channalMessageMap.keySet().contains(text)) {
//            answer.append(Constant.channalMessageMap.get(text));
//            sendMessage = new SendMessage()
//                    .setChatId(chat_id)
//                    .setText(answer.toString());
//        } else {
//            return generalAnswer(text,channelPost.getChatId());
//        }

        return sendMessage;
    }


    private SendMessage generalAnswer(String msg, Long chat_id) {
        String url = "https://api.ownthink.com/bot";
        String param = "appid="+Constant.OWNTHINK_APPID+"&userid="+Constant.OWNTHINK_USERID+"&spoken=" + msg;
        String result = HttpUtil.sendGet(url, param);
        String answer = "听不懂！！";

        JSONObject json = JSON.parseObject(result);
        if("success".equals(json.getString("message"))){
            answer = json.getJSONObject("data").getJSONObject("info").getString("text");
        }
        return new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(answer);
    }

    /**
     * 记录日志
     * @param name
     * @param user_id
     * @param username
     * @param txt
     * @param bot_answer
     */
    private void log(String name, Integer user_id, String username, String txt, String bot_answer) {
        logger.debug("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        logger.debug("Message from " + name + ". (id = " + user_id + " username= " + username + ")  Text - " + txt);
        logger.debug("Bot answer: Text - " + bot_answer);
    }

}
