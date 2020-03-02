package com.bot.bots.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface IMsgBotService {

    List<SendMessage> sendWelcome(Message message) throws Exception;

    SendMessage respondPrivateMessage(Message message) throws Exception;

    SendMessage respondGroupMessage(Message message) throws Exception;

    SendMessage respondChannelMessage(Message channelPost) throws Exception;

}
