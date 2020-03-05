package com.bot.controller;

import com.bot.util.SqLiteUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class PublicController {

    @RequestMapping("/")
    public String root(Map<String,Object> map){
        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from welcome where id=-2");
        if(dataList.size()>0){
            map.put("enable",dataList.get(0).get("welcome_text"));
        }
        return "welcome";
    }

    @RequestMapping("/message")
    public String message(Map<String,Object> map){
        return "message";
    }

    @RequestMapping("/welcome")
    public String welcome(Map<String,Object> map){

        return "welcome";
    }
}
