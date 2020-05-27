package com.k.rabbit.controller;

import com.k.rabbit.service.IRabbitMQConsumerService;
import com.k.rabbit.service.IRabbitMQProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/3/6 9:09
 **/
@Controller
@RequestMapping("rabbit")
public class RabbitMQController {
    @Autowired
    private IRabbitMQConsumerService consumerService;

    @Autowired
    private IRabbitMQProviderService providerService;

    @RequestMapping("hi")
    @ResponseBody
    public String sayHi(){
        return "hello";
    }


    @RequestMapping("send")
    @ResponseBody
    public String sendMq(String message){
        providerService.send(message);
        return "success";
    }


    @RequestMapping("get")
    @ResponseBody
    public String getMq(){
        return consumerService.getMessage();
    }
}
