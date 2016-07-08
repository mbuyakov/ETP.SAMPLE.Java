package ru.codeunited.springmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Created by Igor on 2014.07.31.
 */
public class MQMessageListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(MQMessageListener.class.getName());

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Storage storage;

    @Override
    public void onMessage(Message message) {
        try {
            final String messageId = message.getJMSMessageID();
            LOG.info(
                    format(
                            "\n%s got message %s\nType: %s",
                            Thread.currentThread().getName(),
                            messageId,
                            message.getClass().getName()
                    )
            );

            if (isTextMessage(message)) {
                // Process text message
                final TextMessage textMessage = (TextMessage) message;
               
                storage.store(message);

                // ready for reply
                final Destination replyTo = message.getJMSReplyTo();
                if (replyTo == null) { // quite eating messages without JMSReplyTo
                    LOG.warning(format("Message %s comes without JMSReplyTo", messageId));

                } else {
                    reply(messageId, replyTo);
                }
            } else {
                final String wrongTypeMessage = "We don't handle messages other then TextMessage.";
                LOG.warning(wrongTypeMessage);
                throw new JMSException(wrongTypeMessage);
            }

        } catch (JMSException e) {
            LOG.severe(e.getMessage());
            // and put to DLQ or whatever...
        }
    }

    private void reply(final String collecationId, Destination replyTo) throws JMSException {
        LOG.info("Reply to " + replyTo);
        final AtomicReference<TextMessage> reposentRef = new AtomicReference<TextMessage>();
        jmsTemplate.send(replyTo, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                final TextMessage response = session.createTextMessage();
                response.setJMSCorrelationID(collecationId);
                response.setText("Ok");
                response.setStringProperty("ToOrgCode", "200902");
                response.setStringProperty("FromOrgCode", "4028");
                reposentRef.set(response);
                return response;
            }
        });
        LOG.info(format("Sent reply for %s with MessageId=%s", collecationId, reposentRef.get().getJMSMessageID()));
    }

    public boolean isTextMessage(Message message) {
        return message instanceof TextMessage;
    }
}
