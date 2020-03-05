package com.bot.init;

import com.bot.util.SqLiteUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Component
public class InitSqlite implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String url = SqLiteUtil.getUrl();
        Path path = Paths.get(url);
        if(Files.notExists(path)){
            SqLiteUtil.createTable("create table welcome(\n" +
                    "                    id INTEGER\n" +
                    "                    primary key autoincrement,\n" +
                    "                    welcome_text\n" +
                    "            );");
            SqLiteUtil.createTable("create table message(\n" +
                    "                    id INTEGER\n" +
                    "                    primary key autoincrement,\n" +
                    "                    message_key text,\n" +
                    "                    message_value text\n" +
                    "            );");
            SqLiteUtil.createTable("create table channel_message(\n" +
                    "                    id INTEGER\n" +
                    "                    primary key autoincrement,\n" +
                    "                    message_key text,\n" +
                    "                    message_value text\n" +
                    "            );");

            SqLiteUtil.updateValue("insert into welcome(id,welcome_text) values(-1,'welcome'),(-2,'enable')",new ArrayList<>());
        }
    }

}
