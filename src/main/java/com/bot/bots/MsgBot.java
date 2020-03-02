package com.bot.bots;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bot.bots.service.IMsgBotService;
import com.bot.data.Constant;
import com.bot.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

//@Component
public class MsgBot extends TelegramLongPollingBot {

    private Logger logger = LoggerFactory.getLogger(MsgBot.class);

    private ExecutorService pool = Executors.newCachedThreadPool();

    @Resource
    private IMsgBotService msgBotService;

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

        try {
            logger.debug("json:{}", JSON.toJSONString(update));

            Message messageObj = update.getMessage();

            //10分钟之前的消息不回
            if(new Date(Long.valueOf(messageObj.getDate()+"000")).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().compareTo(LocalDateTime.now().minusMinutes(10)) == -1){
                return;
            }

            if(messageObj.getChat().getTitle() != null && messageObj.getChat().getTitle().equals("DotWallet Official")){
                return;
            }

            //致欢迎
            try {
                List<SendMessage> sendMessageList = msgBotService.sendWelcome(messageObj);
                if(!CollectionUtils.isEmpty(sendMessageList)){
                        for (SendMessage sendMessage : sendMessageList) {
                                execute(sendMessage);
                        }
                    }
                }
            catch (Exception e) {
                logger.error("welcome error:" ,e);
            }

            //如果是消息
            if (update.hasMessage() && update.getMessage().hasText()) {

                //是私聊
                if( messageObj.getChat().isUserChat() ) {
                    try {
                        SendMessage sendMessage = msgBotService.respondPrivateMessage(messageObj);
                        if( sendMessage != null ){
                            execute(sendMessage);
                        }
                    } catch (Exception e) {
                        logger.error("respondPrivateMessage:",e);
                    }

                }
                //是超群组或群组聊
                else if( messageObj.getChat().isSuperGroupChat() || messageObj.getChat().isGroupChat() ) {

                    try {
                        SendMessage sendMessage = msgBotService.respondGroupMessage(messageObj);
                        if( sendMessage != null ){
                            execute(sendMessage);
                        }
                    } catch (Exception e) {
                        logger.error("respondPrivateMessage:",e);
                    }

                }


            }
            //如果是频道消息
            else if (update.hasChannelPost()) {
    //            try {
    //                SendMessage sendMessage = msgBotService.respondChannelMessage(messageObj);
    //                if( sendMessage!=null ){
    //                    execute(sendMessage);
    //                }
    //            } catch (Exception e) {
    //                logger.error("respondPrivateMessage:",e);
    //            }
            }
        } catch (Exception e) {
            logger.error("error ",e);
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

}
