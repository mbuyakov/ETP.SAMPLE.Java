package ru.codeunited.dao;


import org.springframework.stereotype.Repository;

/**
 * @author Natalia Andrianova on 17.08.2016.
 */
@Repository
public class MessageOutQueueDao extends AbstractDao {

    public static final String TABLE_NAME = "msg_out";
    public static final String INSERT = "INSERT INTO " + TABLE_NAME + " (msg_id, body, queue_name) VALUES (?,?,?)";
    public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    public static final String SELECT_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE msg_id = ?";

    public MessageOutQueueDao() {
    }

    @Override
    protected String getInsertStatement() {
        return INSERT;
    }

    @Override
    protected String getSelectAllStatement() {
        return SELECT_ALL;
    }
}
