package ru.codeunited.dao;

import org.springframework.beans.factory.annotation.Autowired;
import ru.codeunited.model.MessageEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Natalia Andrianova on 23.08.2016.
 */
public abstract class AbstractDao implements CrudDao<MessageEntity> {

    @Autowired
    private DataSource dataSource;
    private static final Logger LOG = Logger.getLogger(AbstractDao.class.getName());

    public AbstractDao() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected abstract String getInsertStatement();

    protected abstract String getSelectAllStatement();

    public List<MessageEntity> getAll() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(getSelectAllStatement());
            List<MessageEntity> resultList = new ArrayList<MessageEntity>();
            while (resultSet.next()) {
                MessageEntity message = createInstanceFromResult(resultSet);
                resultList.add(message);
            }
            resultSet.close();
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void insert(MessageEntity message) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getInsertStatement());
            preparedStatement.setString(1, message.getMsgId());
            preparedStatement.setString(2, message.getBody());
            preparedStatement.setString(3, message.getQueueName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageEntity createInstanceFromResult(ResultSet resultSet) throws SQLException {
        MessageEntity message = new MessageEntity();
        message.setMsgId(resultSet.getString("msg_id"));
        message.setBody(resultSet.getString("body"));
        message.setQueueName(resultSet.getString("queue_name"));
        Timestamp timestamp = resultSet.getTimestamp("created");
        java.util.Date date = timestamp == null ? null : new java.util.Date(timestamp.getTime());
        message.setCreated(date);
        return message;
    }
}
