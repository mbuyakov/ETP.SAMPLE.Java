package ru.codeunited.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.codeunited.model.MessageEntity;
import ru.codeunited.service.MessageIncQueueService;
import ru.codeunited.service.MessageOutQueueService;
import ru.codeunited.springmq.MQMessageSender;

import javax.jms.JMSException;
import java.util.logging.Logger;

/**
 * Created by Nat on 12.08.2016.
 */
@Controller
public class MQMessageSenderController {

    private static final Logger LOG = Logger.getLogger(MQMessageSenderController.class.getName());

    @Autowired
    private MQMessageSender mqMessageSender;

    @Autowired
    private MessageOutQueueService messageOutQueueService;

    @Autowired
    private MessageIncQueueService messageIncQueueService;

    @ResponseBody
    @RequestMapping(value = {"/send"}, method = RequestMethod.GET)
    public ModelAndView showSendForm() {
        ModelAndView modelAndView = new ModelAndView("tabs");
        modelAndView.addObject("messagesOut", messageOutQueueService.getAll());
        modelAndView.addObject("messagesInc", messageIncQueueService.getAll());
        return modelAndView;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public void sendMessage(@RequestParam("message") String message,
                            @RequestParam("queue") String queue) {
        String messageId;
        MessageEntity messageEntity = new MessageEntity();
        try {
            messageId = mqMessageSender.send(message, queue);
            messageEntity.setMsgId(messageId);
            messageEntity.setBody(message);
            messageEntity.setQueueName(queue);
            messageOutQueueService.insert(messageEntity);
            LOG.info("Message  " + message + " sent to queue " + queue + ", saved to DB, messageId = [" + messageId + "]");
        } catch (JMSException e) {
            LOG.severe("Message " + message + " was not sent to queue " + queue);
        }
    }
}
