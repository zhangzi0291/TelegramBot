package com.bot.bots;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bot.data.Constant;
import com.bot.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

//@Component
public class MsgBot extends TelegramLongPollingBot {

    private Logger logger = LoggerFactory.getLogger(MsgBot.class);

    private ExecutorService pool = Executors.newCachedThreadPool();

    public MsgBot() {
    }

    public MsgBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotToken() {
        return Constant.MSG_BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        logger.debug("json:{}", JSON.toJSONString(update));

        SendMessage message = null;
        String answer = null;

        String name = "";
        String user_id = "";
        String user_username = "";
        String message_text = "";
        long chat_id = 0;

        //如果是私聊消息
        if (update.hasMessage() && update.getMessage().hasText()) {

            message_text = update.getMessage().getText();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            name = user_first_name + " " + user_last_name;
            user_username = update.getMessage().getChat().getUserName();
            user_id = update.getMessage().getChat().getId()+"";
            chat_id = update.getMessage().getChatId();

            //在问题表中有的话使用问题表的回答，否则使用思知的回答
            if (Constant.oneToOne.keySet().contains(message_text)) {
                answer = Constant.oneToOne.get(message_text);
                message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(answer);
            }


            //如果是频道消息
        } else if (update.hasChannelPost()) {
            message_text = update.getChannelPost().getText();
            chat_id = update.getChannelPost().getChatId();

            answer = message_text;

            user_username = update.getChannelPost().getChat().getUserName();
            user_id = update.getChannelPost().getChat().getId()+"";

            String atName = "";
            //获取消息中是否有@的第一个人
            if (update.getChannelPost().getEntities() != null) {
                if (update.getChannelPost().getEntities().size() > 0) {
                    MessageEntity entity = update.getChannelPost().getEntities().get(0);
                    atName = entity.getText();
                }
            }
            //判断@的是不是本机器人，如果是的话使用一对一问题表，否则用一对多问题表，@需要在文本最后
            if (("@" + getBotUsername()).equals(atName)) {
                message_text = message_text.substring(0, update.getChannelPost().getEntities().get(0).getOffset());

                if (Constant.oneToOne.keySet().contains(message_text.trim())) {
                    answer = Constant.oneToOne.get(message_text.trim());
                    message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText(answer);
                } else {
                    message = generalAnswer(message_text, chat_id);
                }
            } else if (Constant.oneToMany.keySet().contains(message_text)) {
                answer = Constant.oneToMany.get(message_text);
                message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(answer);
            }


        }

        try {
            if(message != null){
                execute(message); // Sending our message object to user
            }else{
                message = generalAnswer(message_text, chat_id);
                execute(message); // Sending our message object to user
            }
            log(name, user_id, user_username, message_text, message.getText());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return Constant.MSG_BOT_NAME;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for (Update update : updates) {
            pool.submit(new Thread(()->onUpdateReceived(update)));

        }
    }

    /**
     * 记录日志
     * @param name
     * @param user_id
     * @param username
     * @param txt
     * @param bot_answer
     */
    private void log(String name, String user_id, String username, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        logger.debug("Message from " + name + ". (id = " + user_id + " username= " + username + ")  Text - " + txt);
        logger.debug("Bot answer: Text - " + bot_answer);
    }

    /**
     * 思知的通用回答
     * @param msg
     * @param chat_id
     * @return
     */
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

}
