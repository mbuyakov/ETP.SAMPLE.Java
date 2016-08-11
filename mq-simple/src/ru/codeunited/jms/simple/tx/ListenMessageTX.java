package ru.codeunited.jms.simple.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codeunited.jms.service.*;

import javax.jms.*;

import static ru.codeunited.jms.simple.JmsHelper.connect;
import static ru.codeunited.jms.simple.JmsHelper.getConnectionFactory;
import static ru.codeunited.jms.simple.JmsHelper.resolveQueue;
import static ru.codeunited.jms.simple.SendMessageTX.sendMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 12.10.15.
 */
public class ListenMessageTX {

    private static final Logger LOG = LoggerFactory.getLogger(ListenMessageTX.class);

    private static final BusinessService service = new BusinessNumberServiceImpl();

    private static final MessageLoggerService logService = new MessageLoggerServiceImpl();

    private static final String TARGET_QUEUE = "SAMPLE.APPLICATION_INC";

    private static final String STATUS_QUEUE = "SAMPLE.STATUS_OUT";

    private static final String STATUS_MESSAGE_OK = "Сообщение получено: ";

    private static final long SHUTDOWN_TIMEOUT = 10000L;

    public static void main(String[] args) throws JMSException, InterruptedException {
        ConnectionFactory connectionFactory = getConnectionFactory();
        Connection connection = connect(connectionFactory);
        final Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

        Queue applicationQueue = resolveQueue(TARGET_QUEUE, session);

        MessageConsumer consumer = session.createConsumer(applicationQueue);
        Queue statusQueue = resolveQueue(STATUS_QUEUE, session);
        MessageProducer producer = session.createProducer(statusQueue);
        consumer.setMessageListener(new LogMessageListenerTX(session, service, logService, producer));

        connection.start();     // !DON'T FORGET!

        LOG.debug("Listen messages for {}ms...", SHUTDOWN_TIMEOUT);
        Thread.currentThread().join(SHUTDOWN_TIMEOUT); // Consume only 10 second

        // release resources
        connection.stop();
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }

    private static class LogMessageListenerTX implements MessageListener {

        private final Session session;

        private final BusinessService service;

        private final MessageLoggerService loggerService;

        private MessageProducer producer;

        public LogMessageListenerTX(Session session, BusinessService service, MessageLoggerService loggerService,
                                    MessageProducer producer) throws JMSException {
            this.session = session;
            this.service = service;
            this.loggerService = loggerService;
            this.producer = producer;
        }

        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            loggerService.incoming(message);
            try {

                BusinessResponse response = service.processRequest(new BusinessRequest(textMessage.getText()));

                String resultMessageBody = STATUS_MESSAGE_OK + textMessage.getText();
                Message resultMessage = sendMessage(session, producer, resultMessageBody);
                LOG.debug("Send message ID [{}]. Body [{}]", resultMessage.getJMSMessageID(), resultMessageBody);
                session.commit();
                loggerService.handled(message);

            } catch (Exception e) {
                loggerService.error(message, e);

                try {
                    session.rollback();
                    loggerService.rollback(message);
                } catch (JMSException e1) {
                    LOG.error(e1.getMessage());
                }
            }
        }
    }
}
