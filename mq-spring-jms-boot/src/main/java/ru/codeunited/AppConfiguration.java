package ru.codeunited;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQDestination;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

@Configuration
public class AppConfiguration {

    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) throws JMSException {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        ((MQConnectionFactory) connectionFactory).setTransportType(1);
        ((MQConnectionFactory) connectionFactory).setCCSID(1208);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ConnectionFactory connectionFactory) throws JMSException {
        ((MQConnectionFactory) connectionFactory).setTransportType(1);
        ((MQConnectionFactory) connectionFactory).setCCSID(1208);
        ((MQConnectionFactory) connectionFactory).setConnectionNameList("etp3.sm-soft.ru(2424),etp4.sm-soft.ru(2424)");
        return new JmsMessagingTemplate(connectionFactory);
    }

    @Bean
    public DefaultMessageListenerContainer consumerJmsListenerContainer(ConnectionFactory connectionFactory) throws JMSException {
        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        ((MQConnectionFactory) connectionFactory).setTransportType(1);
        ((MQConnectionFactory) connectionFactory).setCCSID(1208);
        messageListenerContainer.setSessionTransacted(true);
        messageListenerContainer.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        messageListenerContainer.setDestination(new MQDestination("SAMPLE.APPLICATION_INC"));
        messageListenerContainer.setMessageListener(new Listener());
        messageListenerContainer.setConnectionFactory(connectionFactory);
        return messageListenerContainer;
    }

}
