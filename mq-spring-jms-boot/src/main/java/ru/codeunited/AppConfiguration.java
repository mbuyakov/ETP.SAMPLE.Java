package ru.codeunited;

import com.ibm.mq.jms.MQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

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
        return new JmsMessagingTemplate(connectionFactory);
    }


}
