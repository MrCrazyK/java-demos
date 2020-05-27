package com.k.kafka.service.impl;

import com.k.kafka.config.KafkaQueueConfig;
import com.k.kafka.service.IKafkaProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * @author k
 */
@Service
public class KafkaProviderServiceImpl implements IKafkaProviderService {


    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void send(String name) {
        jmsTemplate.send(KafkaQueueConfig.QUEUE_NAME, session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(name);
            return textMessage;
        });
    }
}
