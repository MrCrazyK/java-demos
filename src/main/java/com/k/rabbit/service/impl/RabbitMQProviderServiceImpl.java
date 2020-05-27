package com.k.rabbit.service.impl;

import com.k.rabbit.config.RabbitMQQueueConfig;
import com.k.rabbit.service.IRabbitMQProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * @author k
 */
@Service
public class RabbitMQProviderServiceImpl implements IRabbitMQProviderService {


    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void send(String name) {
        jmsTemplate.send(RabbitMQQueueConfig.QUEUE_NAME, session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(name);
            return textMessage;
        });
    }
}
