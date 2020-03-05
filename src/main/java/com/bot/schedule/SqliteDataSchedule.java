package com.bot.schedule;


import com.bot.data.Constant;
import com.bot.util.CsvParser;
import com.bot.util.SqLiteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SqliteDataSchedule {

    private Logger logger = LoggerFactory.getLogger(SqliteDataSchedule.class);
    @Value("${chat.message}")
    private String messagePath;
    @Value("${chat.channalMessage}")
    private String channalMessagePath;
    @Value("${chat.welcomeText}")
    private String welcomeTextPath;

    @Scheduled(fixedRate = 1000*60*5)
    public void loadOneToOneData() throws Exception{
        logger.debug("更新数据：{}",messagePath);
        Map<String,String> resultMap = new HashMap();

        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from message");
        for (Map<String, String> map : dataList) {
            resultMap.put(map.get("message_key"),map.get("message_value"));
        }
        Constant.messageMap = resultMap;

    }
    @Scheduled(fixedRate = 1000*60*5)
    private void loadOneToManyData() throws Exception{
        logger.debug("更新数据：{}",channalMessagePath);
        Map<String,String> resultMap = new HashMap();

        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from channel_message");
        for (Map<String, String> map : dataList) {
            resultMap.put(map.get("message_key"),map.get("message_value"));
        }
        Constant.channalMessageMap = resultMap;
    }

    @Scheduled(fixedRate = 1000*60*5)
    private void welcomeText() throws Exception{
        logger.debug("更新数据：{}",welcomeTextPath);

        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from welcome");
        if(dataList.size()>0){
            Constant.WELCOME_TEXT = dataList.get(0).get("welcome_text");
        }
    }
}
