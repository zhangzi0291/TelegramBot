package com.bot.schedule;


import com.bot.data.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSchedule {

    @Value("${chat.onetoone}")
    private String oneToOneFilePath;
    @Value("${chat.onetomany}")
    private String oneToManyFilePath;

    @Scheduled(fixedRate = 1000*60*5)
    private void loadOneToOneData() throws Exception{
        System.out.println(oneToOneFilePath);
        Path filePath = Paths.get(oneToOneFilePath);
        List<String> list = Files.readAllLines(filePath);
        Map<String,String> resultMap = new HashMap();

        list.forEach(s -> {
            String[] strs = s.split(",");
            if(!StringUtils.isEmpty(strs[0]) && !StringUtils.isEmpty(strs[1])) {
                if(strs[1].startsWith("\"") && strs[1].endsWith("\"")) {
                    strs[1] = strs[1].substring(1,strs[1].length()-1);
                }
                resultMap.put(strs[0], strs[1]);
            }
        });
        Constant.oneToOne = resultMap;

    }
    @Scheduled(fixedRate = 1000*60*5)
    private void loadOneToManyData() throws Exception{
        System.out.println(oneToManyFilePath);
        Path filePath = Paths.get(oneToManyFilePath);
        List<String> list = Files.readAllLines(filePath);
        Map<String,String> resultMap = new HashMap();

        list.forEach(s -> {
            String[] strs = s.split(",");
            if(!StringUtils.isEmpty(strs[0]) && !StringUtils.isEmpty(strs[1])) {
                if(strs[1].startsWith("\"") && strs[1].endsWith("\"")) {
                    strs[1] = strs[1].substring(1,strs[1].length()-1);
                }
                resultMap.put(strs[0], strs[1]);

            }
        });
        Constant.oneToMany = resultMap;
    }
}
