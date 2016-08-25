package ru.codeunited.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codeunited.dao.MessageOutQueueDao;
import ru.codeunited.model.MessageEntity;

/**
 * @author Natalia Andrianova on 24.08.2016.
 */
@Service
public class MessageOutQueueService extends AbstractService {

    public MessageOutQueueService() {
    }

    @Autowired
    public MessageOutQueueService(MessageOutQueueDao dao) {
        super(dao);
    }
}
