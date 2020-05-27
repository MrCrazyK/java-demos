package com.k.kafka.service.impl;


import com.k.kafka.config.KafkaQueueConfig;
import com.k.kafka.service.IKafkaConsumerService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;


/**
 * @author k
 */
@Service
public class KafkaConsumerServiceImpl implements IKafkaConsumerService {


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
    @JmsListener(destination = KafkaQueueConfig.QUEUE_NAME)
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
    @JmsListener(destination = KafkaQueueConfig.QUEUE_NAME)
    public void getMessage2(String msg) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功接受msg2" + msg);
    }
}
