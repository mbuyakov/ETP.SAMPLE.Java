package ru.codeunited.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.codeunited.dao.MessageIncQueueDao;
import ru.codeunited.model.MessageEntity;

/**
 * @author Natalia Andrianova on 24.08.2016.
 */
@Service
public class MessageIncQueueService extends AbstractService {

    public MessageIncQueueService() {
    }

    @Autowired
    public MessageIncQueueService(MessageIncQueueDao dao) {
        super(dao);
    }
}
