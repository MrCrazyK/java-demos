package com.k.active.service.impl;

import com.k.active.config.ActiveMQQueueConfig;
import com.k.active.service.IActiveMQProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * @author k
 */
@Service
public class ActiveMQProviderServiceImpl implements IActiveMQProviderService {


    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void send(String name) {
        jmsTemplate.send(ActiveMQQueueConfig.QUEUE_NAME, session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(name);
            return textMessage;
        });
    }
}
