package ru.codeunited.model;

import java.util.Date;

/**
 * Created by Nat on 17.08.2016.
 */
public class MessageEntity {

    private String msgId;

    private String body;

    private String queueName;

    private Date created;

    public MessageEntity() {
    }

    public MessageEntity(String body, String queueName) {
        this.body = body;
        this.queueName = queueName;
    }

    public MessageEntity(String body) {
        this.body = body;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "msgId='" + msgId + '\'' +
                ", body='" + body + '\'' +
                ", created=" + created +
                '}';
    }
}
