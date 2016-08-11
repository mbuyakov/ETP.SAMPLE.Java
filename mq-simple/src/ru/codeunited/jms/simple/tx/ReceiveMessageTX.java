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
public class ReceiveMessageTX {

    private static final Logger LOG = LoggerFactory.getLogger(ReceiveMessageTX.class);

    private static final BusinessService service = new BusinessNumberServiceImpl();

    private static final MessageLoggerService logService = new MessageLoggerServiceImpl();

    private static final String APPLICATION_QUEUE = "SAMPLE.APPLICATION_INC";

    private static final String STATUS_QUEUE = "SAMPLE.STATUS_OUT";
    private static final String STATUS_MESSAGE_OK = "Сообщение получено: ";

    private static final long TIMEOUT = 1000L;

    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = getConnectionFactory();
        Connection connection = connect(connectionFactory);

        // WORK UNIT START
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue applicationQueue = resolveQueue(APPLICATION_QUEUE, session);
        Queue statusQueue = resolveQueue(STATUS_QUEUE, session);
        MessageConsumer consumer = session.createConsumer(applicationQueue);
        MessageProducer producer = session.createProducer(statusQueue);

        connection.start();

        TextMessage receivedMessage = (TextMessage) consumer.receive(TIMEOUT);
        while (receivedMessage != null) {
            logService.incoming(receivedMessage);

            try {
                BusinessResponse response = service.processRequest(new BusinessRequest(receivedMessage.getText()));
                String resultMessageBody = STATUS_MESSAGE_OK + receivedMessage.getText();
                Message resultMessage = sendMessage(session, producer, resultMessageBody);
                LOG.debug("Send message ID [{}]. Body [{}]", resultMessage.getJMSMessageID(), resultMessageBody);
                session.commit(); // We use transacted session
                LOG.info("Commit performed.");
                logService.handled(receivedMessage);
            } catch (Exception e) {
                logService.error(receivedMessage, e);
                session.rollback();
                logService.rollback(receivedMessage);
            }
            receivedMessage = (TextMessage) consumer.receive(TIMEOUT);
        }
        LOG.info("Queue is empty");
        // WORK UNIT END

        // release resources
        connection.stop();
        consumer.close();
        producer.close();
        session.close();
        connection.close();
    }
}
