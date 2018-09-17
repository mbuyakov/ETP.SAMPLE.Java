package ru.codeunited;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class ListenerWithAnnotation {

    private final Logger LOG = LoggerFactory.getLogger(ListenerWithAnnotation.class);

    @JmsListener(destination = "SAMPLE.APPLICATION_INC", containerFactory = "myFactory")
    public void receiveMessage(Message<String> message) {
        LOG.info("Received message <" + message.getPayload() + ">");
    }
}
