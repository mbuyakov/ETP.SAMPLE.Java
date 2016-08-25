package ru.codeunited.springmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.codeunited.model.MessageEntity;
import ru.codeunited.service.MessageIncQueueService;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Igor on 2014.08.13.
 */
@Component
public class FileStorage implements Storage {

    private static final Logger LOG = Logger.getLogger(FileStorage.class.getName());

    public static final String EXTENSION = ".dat";
    public static final String REGEX_NUMBERS_ONLY = "\\d+";

    private File destinationFolder;

    @Autowired
    private MessageIncQueueService messageIncQueueService;

    @PostConstruct
    public void postConstruction() {
        destinationFolder = new File("/tmp/" + FileStorage.class.getSimpleName());
        destinationFolder.mkdirs();
    }

    private String createAbsoluteFileName(final Message message) throws JMSException {
        return destinationFolder.getAbsoluteFile() + File.separator + message.getJMSMessageID().replaceAll(":", "_") + EXTENSION;
    }

    @Override
    public boolean store(final Message message) throws JMSException {
        return store((TextMessage) message);
    }

    private boolean store(final TextMessage textMessage) throws JMSException {
        try {
            final String textPayload = textMessage.getText();
            
            if(textPayload.startsWith("error")) {
//            if(!textPayload.matches(REGEX_NUMBERS_ONLY)) {
                LOG.severe("Message contains not only numbers");
                throw new RuntimeException("error in message");
            }
            final FileOutputStream stream = new FileOutputStream(createAbsoluteFileName(textMessage));
            stream.write(textPayload.getBytes("UTF-8"));
            stream.close();
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMsgId(textMessage.getJMSMessageID());
            messageEntity.setBody(textPayload);
//            messageEntity.setQueueName(textMessage.getStringProperty());
            messageIncQueueService.insert(messageEntity);
            LOG.info("Message " + textPayload + " saved in DB");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}