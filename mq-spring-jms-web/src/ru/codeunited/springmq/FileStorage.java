package ru.codeunited.springmq;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Igor on 2014.08.13.
 */
@Component
public class FileStorage implements Storage {

    public static final String EXTENSION = ".dat";

    private File destinationFolder;

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
            	throw new RuntimeException("error in message");
            }
            final FileOutputStream stream = new FileOutputStream(createAbsoluteFileName(textMessage));
            stream.write(textPayload.getBytes("UTF-8"));
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}