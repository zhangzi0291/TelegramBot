package com.bot.controller;

import com.bot.data.Constant;
import com.bot.util.SqLiteUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("data")
public class DataController {

    @RequestMapping("getMessageData")
    public List<Map<String, String>>  getMessageData(){
        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from message");
        return dataList;
    }

    @RequestMapping("deleteMessageData")
    public String deleteMessageData(Integer id){
        List<String> params = new ArrayList();
        params.add(id.toString());
        SqLiteUtil.updateValue("delete from message where id = ?",params);
        return "success";
    }

    @RequestMapping("getMessageDetail")
    public Map<String, String> getMessageDetail(Integer id){
        List<String> params = new ArrayList();
        params.add(id.toString());
        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from message where id=?",params);

        if(dataList.size()>0){
            return dataList.get(0);
        }
        return new HashMap<>();
    }

    @RequestMapping("updateMessage")
    public String updateMessage(String id,String message_key,String message_value){
        List<String> params = new ArrayList();
        params.add(message_key);
        params.add(message_value);
        params.add(id);
        SqLiteUtil.updateValue("update message set message_key=?,message_value=? where id = ?",params);
        return "success";
    }

    @RequestMapping("getWelcomeData")
    public String  getWelcomeData(){
        List<Map<String, String>> dataList = SqLiteUtil.getRowValue("select * from welcome where id=-1");

        if(dataList.size()>0){
            return dataList.get(0).get("welcome_text");
        }
        return "";
    }

    @RequestMapping("changeWelcomeStatus")
    public String changeWelcomeStatus(){
        String welcomeEnable = "disable";
        if(!Constant.WELCOME_ENABLE){
            welcomeEnable = "enable";
        }
        List params = new ArrayList();
        params.add(welcomeEnable);
        SqLiteUtil.updateValue("update welcome set welcome_text = ? where id = -2",params);
        Constant.WELCOME_ENABLE = !Constant.WELCOME_ENABLE;
        return "success";
    }

    @RequestMapping("saveWelcomeText")
    public String saveWelcomeText(String welcomeText){
        List params = new ArrayList();
        params.add(welcomeText);
        SqLiteUtil.updateValue("update welcome set welcome_text = ? where id = -1",params);
        Constant.WELCOME_TEXT = welcomeText;
        return "success";
    }

}
