package com.k.rabbit.service.impl;


import com.k.rabbit.config.RabbitMQQueueConfig;
import com.k.rabbit.service.IRabbitMQConsumerService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;


/**
 * @author k
 */
@Service
public class RabbitMQConsumerServiceImpl implements IRabbitMQConsumerService {


    /**
     * 使用JmsListener配置消费者监听的队列，其中name是接收到的消息
     *
     * @return
     */
    @Override
    public String getMessage() {
        String res = "";
        getMessage(res);
        return res;
    }

    /**
     * 使用JmsListener配置消费者监听的队列，其中name是接收到的消息
     *
     * @param msg
     * @return
     */
    @JmsListener(destination = RabbitMQQueueConfig.QUEUE_NAME)
    public void getMessage(String msg) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功接受msg1" + msg);
    }

    /**
     * 使用JmsListener配置消费者监听的队列，其中name是接收到的消息
     *
     * @param msg
     * @return
     */
    @JmsListener(destination = RabbitMQQueueConfig.QUEUE_NAME)
    public void getMessage2(String msg) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功接受msg2" + msg);
    }
}
