package ru.codeunited.service;

import org.springframework.transaction.annotation.Transactional;
import ru.codeunited.dao.AbstractDao;
import ru.codeunited.model.MessageEntity;

import java.util.List;

/**
 * @author Natalia Andrianova on 23.08.2016.
 */

public class AbstractService {

    private AbstractDao dao;

    public AbstractService() {
    }

    public AbstractService(AbstractDao dao) {
        this.dao = dao;
    }

    public AbstractDao getDao() {
        return dao;
    }

    public void setDao(AbstractDao dao) {
        this.dao = dao;
    }

    public List<MessageEntity> getAll() {
        return this.dao.getAll();
    }

    @Transactional
    public void insert(MessageEntity message) {
        this.dao.insert(message);
    }
}
