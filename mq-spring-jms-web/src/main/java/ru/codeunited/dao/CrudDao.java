package ru.codeunited.dao;

import ru.codeunited.model.MessageEntity;

import java.util.List;

/**
 * @author Natalia Andrianova on 23.08.2016.
 */
public interface CrudDao<T> {

    List<T> getAll();
    void insert(MessageEntity message);
}
