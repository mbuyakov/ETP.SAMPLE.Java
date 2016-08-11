package ru.codeunited.jms.simple.ack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codeunited.jms.service.*;
import ru.codeunited.jms.simple.ExceptionHandlingStrategy;
import ru.codeunited.jms.simple.ack.strategy.BackoutOnExceptionStrategy;

import javax.jms.*;

import static ru.codeunited.jms.simple.JmsHelper.*;
import static ru.codeunited.jms.simple.SendMessageTX.sendMessage;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 12.10.15.
 */
public class ListenMessageACK {

    private static final Logger LOG = LoggerFactory.getLogger(ListenMessageACK.class);

    private static final String TARGET_QUEUE = "SAMPLE.APPLICATION_INC";

    private static final String STATUS_QUEUE = "SAMPLE.STATUS_OUT";

    private static final String BACKOUT_QUEUE = "SAMPLE.APPLICATION_INC.BK";

    private static final String  STATUS_MESSAGE_OK = "Сообщение получено";

    private static final long SHUTDOWN_TIMEOUT = 30000L;

    public static void main(String[] args) throws JMSException, InterruptedException {

        MessageLoggerService logService = new MessageLoggerServiceImpl();
        BusinessService service = new BusinessNumberServiceImpl();

        ConnectionFactory connectionFactory = getConnectionFactory();
        Connection connection = connect(connectionFactory);
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = resolveQueue(TARGET_QUEUE, session);
        Queue statusQueue = resolveQueue(STATUS_QUEUE, session);
        MessageProducer producer = session.createProducer(statusQueue);
        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener(
                new LogMessageListenerACK(service, logService, session,producer)
                        .setExceptionHandlingStrategy(new BackoutOnExceptionStrategy(BACKOUT_QUEUE, logService))
                //.setExceptionHandlingStrategy(new RecoverOnException())
        );

        connection.start();     // !DON'T FORGET!

        LOG.debug("Listen messages for {}ms...", SHUTDOWN_TIMEOUT);
        Thread.sleep(SHUTDOWN_TIMEOUT);

        // release resources
        connection.stop();
        consumer.close();
        producer.close();
        session.close();
        connection.close();

    }

    private static class LogMessageListenerACK implements MessageListener {
        private MessageProducer producer;

        private final BusinessService service;

        private final MessageLoggerService loggerService;

        private final Session session;

        private ExceptionHandlingStrategy exceptionHandlingStrategy;


       /* private LogMessageListenerACK(BusinessService service, MessageLoggerService loggerService, Session session) throws JMSException {
            this.service = service;
            this.loggerService = loggerService;
            this.session = session;
            this.statusQueue = resolveQueue(STATUS_QUEUE, session);
            this.producer = session.createProducer(statusQueue);*/

        private LogMessageListenerACK(BusinessService service, MessageLoggerService loggerService, Session session, MessageProducer producer) throws JMSException {
            this.service = service;
            this.loggerService = loggerService;
            this.session = session;
            this.producer = producer;

        }

        public LogMessageListenerACK setExceptionHandlingStrategy(ExceptionHandlingStrategy exceptionHandlingStrategy) {
            this.exceptionHandlingStrategy = exceptionHandlingStrategy;
            return this;
        }

        @Override
        public void onMessage(Message message) {
            TextMessage textMessage = (TextMessage) message;
            loggerService.incoming(textMessage);
            try {

                BusinessResponse response = service.processRequest(new BusinessRequest(textMessage.getText()));

                message.acknowledge();

                String resultMessageBody = STATUS_MESSAGE_OK + " [ " + textMessage.getText() + " ] ";
                Message resultMessage = sendMessage(session, producer, resultMessageBody);
                LOG.info("Send message ID [{}]. Body [{}]", resultMessage.getJMSMessageID(), resultMessageBody);
                loggerService.handled(message);

            } catch (Exception e) {
                loggerService.error(message, e);
                exceptionHandlingStrategy.handle(session, message, e);
            }
        }

    }
}
