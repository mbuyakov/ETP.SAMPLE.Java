package ru.codeunited.springmq;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by Igor on 2014.08.13.
 */
public interface Storage {

    public boolean store(Message message) throws JMSException;

}
