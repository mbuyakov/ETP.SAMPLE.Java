package ru.codeunited;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class Listener {

    private final Logger LOG = LoggerFactory.getLogger(Listener.class);

    @JmsListener(destination = "${ibm.mq.channel}", containerFactory = "myFactory")
    public void receiveMessage(Message<String> message) {
        LOG.info("Received message <" + message.getPayload() + ">");
    }
}
