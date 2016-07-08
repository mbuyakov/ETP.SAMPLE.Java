package ru.codeunited.simplemq;


import javax.jms.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

public class MQMessageListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(MQMessageListener.class.getName());
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    @Override
    public void onMessage(Message message) {
        try {
        	System.out.println("MQMessageListener.onMessage() ");
            final String messageId = message.getJMSMessageID();
            LOG.info(
                    format("\n%s got message %s\nType: %s",
                            Thread.currentThread().getName(),
                            messageId,
                            message.getClass().getName()
                    )
            );

            if (isTextMessage(message)) {
                // Process text message
                final TextMessage textMessage = (TextMessage) message;
                LOG.info(textMessage.getText());
                System.out.println("MQMessageListener.onMessage() " + textMessage.getText());
                if(textMessage.getText().startsWith("ERROR")) {
                	throw new RuntimeException();
                }
            } else {
                String errorMessage = String.format("We don't handle messages other then TextMessage." +
                        "Got :%s for messageID %s", message.getClass().getName(), messageId);
                throw new Exception(errorMessage);
            }
        } catch (RuntimeException e){
            LOG.log(Level.SEVERE, "Error while process incoming message", e);
            throw e;
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean isTextMessage(Message message) {
        return message instanceof TextMessage;
    }

    public void shutdown() {
        try {
            if(consumer != null){
                consumer.close();
                session.close();
                connection.close();
            }
        } catch (JMSException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Can't shutdown listener", e);
        }
    }

    public void subscribe(Connection connection) throws JMSException {
        this.connection = connection;
        session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        consumer = session.createConsumer(session.createQueue("MVK.TEST_INC"));
        consumer.setMessageListener(this);
    	System.out.println("MQMessageListener.subscribe() connection.start()");
        connection.start();
    }
}
