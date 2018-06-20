package ru.codeunited;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;


@SpringBootApplication
@EnableJms
public class MqSpringJmsBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqSpringJmsBootApplication.class, args);
    }
}
