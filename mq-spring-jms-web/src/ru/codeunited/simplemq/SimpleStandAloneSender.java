package ru.codeunited.simplemq;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * Created by MBuyakov on 14.08.2014.
 */
public class SimpleStandAloneSender {
	
/*	
	public static String host = "test-int-01";
	public static int port = 1414;
	public static String qmgrName = "COREGVCQMGR";
	public static String channelName = "JVM.DEF.SVRCONN";
	public static String login = "mqadm";
	public static String password = "passw0rd";
*/	

/*	
	public static String host = "etp.sm-soft.ru";
	public static int port = 2414;
	public static String qmgrName = "IB9QMGR";
	public static String channelName = "SYSTEM.BKR.CONFIG";
	public static String queueName = "MOSGAZ.APPLICATION_INC";
*/
	
	public static String hosts = "etp3.sm-soft.ru(1414),etp4.sm-soft.ru(1414)";
	public static String qmgrName = "IBMESBQM";
	public static String channelName = "CLIENT.TEST.SVRCONN";
	public static String queueName = "TEST.SHIFT_INC";
	public static String login = "test";
	public static String password = "passw0rd";
	
    public static void main(String[] args) throws JMSException, InterruptedException {
        com.ibm.mq.jms.MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        //factory.setHostName(host);
        //factory.setPort(port);
        factory.setConnectionNameList(hosts);
        factory.setQueueManager(qmgrName);
        factory.setChannel(channelName);
        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT); // TRANSPORT_MQSERIES_CLIENT
        factory.setCCSID(1208);
        factory.setClientReconnectTimeout(600);
        factory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
        

        Connection connection = factory.createConnection(login, password);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(queueName);
        MessageProducer producer = session.createProducer(queue);
        
        TextMessage message;
        for(int i=0; i < 10000; i++) {
	        message = session.createTextMessage("Hellos");
	        producer.send(message);
	        System.out.println("SimpleStandAloneSender.main() " + i + " " +  message.getJMSMessageID() +  " " + new Date());
	        //Thread.sleep(1000);
        }
        
        producer.close();
        session.close();
        connection.close();
    }
}
