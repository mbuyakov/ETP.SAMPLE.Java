package ru.codeunited;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;


@Service
public class Sender {

    private final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void send(String body){
        LOG.info("Try to send message {}", body);
        jmsMessagingTemplate.getJmsTemplate().setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        jmsMessagingTemplate.getJmsTemplate().convertAndSend("SAMPLE.APPLICATION_INC", body);
        LOG.info("Message {} sended", body);

        try {
            String value = this.jmsMessagingTemplate.getJmsTemplate().execute(session -> {
                MessageConsumer consumer = session.createConsumer(
                        this.jmsMessagingTemplate.getJmsTemplate().getDestinationResolver().resolveDestinationName(session, "SAMPLE.APPLICATION_INC", false));
                String result;
                try {
                    Message received = consumer.receive(5000);
                    result = (String) this.jmsMessagingTemplate.getJmsTemplate().getMessageConverter().fromMessage(received);
                    // Do some stuff that might throw an exception
                    received.acknowledge();
                }
                finally {
                    consumer.close();
                }
                return result;
            }, true);
            System.out.println(value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
