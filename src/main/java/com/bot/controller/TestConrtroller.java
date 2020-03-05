package com.bot.controller;

import com.bot.bots.MsgBot;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;

@RestController
public class TestConrtroller {

//    @Resource
//    private MsgBot msgBot;
//
//    @RequestMapping("pushToChannel")
//    public String test(String answer){
//        try {
//            SendMessage message = new SendMessage() // Create a message object object
//                    .setChatId("-1001248166672")
//                    .setText(answer);
//            msgBot.execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//            return "error";
//        }
//
//        return "ok";
//
//    }

}
