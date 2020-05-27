package com.k.active.controller;

import com.k.active.service.IActiveMQConsumerService;
import com.k.active.service.IActiveMQProviderService;
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
@RequestMapping("active")
public class ActiveMQController {
    @Autowired
    private IActiveMQConsumerService consumerService;

    @Autowired
    private IActiveMQProviderService providerService;

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
