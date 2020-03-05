package com.bot.schedule;


import com.bot.bots.MsgBot;
import com.bot.data.Constant;
import com.bot.util.CsvParser;
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

//@Component
public class DataSchedule {

    private Logger logger = LoggerFactory.getLogger(DataSchedule.class);
    @Value("${chat.message}")
    private String messagePath;
    @Value("${chat.channalMessage}")
    private String channalMessagePath;
    @Value("${chat.welcomeText}")
    private String welcomeTextPath;

    @Scheduled(fixedRate = 1000*60*5)
    private void loadOneToOneData() throws Exception{
        logger.debug("更新数据：{}",messagePath);
        Path filePath = Paths.get(messagePath);
        CsvParser csvParser = new CsvParser(filePath, Charset.forName("GBK"));
        List<List<String>> list = csvParser.getRowsWithNoHeader();
        Map<String,String> resultMap = new HashMap();
        list.forEach(row -> {
            if(!StringUtils.isEmpty(row.get(0)) && !StringUtils.isEmpty(row.get(1))) {
                if(row.get(1).startsWith("\"") && row.get(1).endsWith("\"")) {
                    row.set(1,row.get(1).substring(1,row.get(1).length()-1));
                }
                resultMap.put(row.get(0), row.get(1));
            }
        });
        Constant.messageMap = resultMap;

    }
    @Scheduled(fixedRate = 1000*60*5)
    private void loadOneToManyData() throws Exception{
        logger.debug("更新数据：{}",channalMessagePath);
        Path filePath = Paths.get(channalMessagePath);
        CsvParser csvParser = new CsvParser(filePath, Charset.forName("GBK"));
        List<List<String>> list = csvParser.getRowsWithNoHeader();
        Map<String,String> resultMap = new HashMap();
        list.forEach(row -> {
            if(!StringUtils.isEmpty(row.get(0)) && !StringUtils.isEmpty(row.get(1))) {
                if(row.get(1).startsWith("\"") && row.get(1).endsWith("\"")) {
                    row.set(1,row.get(1).substring(1,row.get(1).length()-1));
                }
                resultMap.put(row.get(0), row.get(1));

            }
        });
        Constant.channalMessageMap = resultMap;
    }

    @Scheduled(fixedRate = 1000*60*5)
    private void welcomeText() throws Exception{
        logger.debug("更新数据：{}",welcomeTextPath);
        Path filePath = Paths.get(welcomeTextPath);
        String welcomeText = new String (Files.readAllBytes(filePath));
        Constant.WELCOME_TEXT = welcomeText;
    }
}
