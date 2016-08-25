package ru.codeunited.springmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Created by Igor on 2014.07.31.
 */
public class MQMessageSender {

    private static final Logger LOG = Logger.getLogger(MQMessageSender.class.getName());

    @Autowired
    private JmsTemplate jmsTemplate;

    private String destinationQueue;

    private String replyToQueue;

    public String getDestinationQueue() {
        return destinationQueue;
    }

    public void setDestinationQueue(String destinationQueue) {
        this.destinationQueue = destinationQueue;
    }

    public String getReplyToQueue() {
        return replyToQueue;
    }

    public void setReplyToQueue(String replyToQueue) {
        this.replyToQueue = replyToQueue;
    }

    /**
     * Use injected destinationQueue as target.
     * @param message
     */
    public String send(final String message) throws JMSException {
        return send(message, getDestinationQueue());
    }

    public final String send(final String message, final String destinationQueue) throws JMSException {
        final AtomicReference<TextMessage> messageAtomicReference = new AtomicReference<TextMessage>();
        jmsTemplate.send(destinationQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage tm = session.createTextMessage();
                tm.setText(message);
                if (needReply()) {
                    tm.setJMSReplyTo(jmsTemplate.getDestinationResolver().resolveDestinationName(session, getReplyToQueue(), false));
                }
                messageAtomicReference.set(tm);
                return tm;
            }
        });
        return messageAtomicReference.get().getJMSMessageID();
    }

    private boolean needReply() {
        return getReplyToQueue() != null;
    }
}
