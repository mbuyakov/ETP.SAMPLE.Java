package ru.codeunited;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(Listener.class);

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            LOG.info("Consumed message: " + msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
