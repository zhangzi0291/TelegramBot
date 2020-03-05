package com.bot.init;

import com.bot.data.Constant;
import com.bot.util.SqLiteUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class InitData implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Map<String, String>> enableList = SqLiteUtil.getRowValue("select * from welcome where id=-2");
        if(enableList.size()>0){
            Constant.WELCOME_ENABLE = enableList.get(0).get("welcome_text").equalsIgnoreCase("enable");
        }
        List<Map<String, String>> welcomeList = SqLiteUtil.getRowValue("select * from welcome where id=-1");
        if(welcomeList.size()>0){
            Constant.WELCOME_TEXT = welcomeList.get(0).get("welcome_text");
        }
    }
}
