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
import java.util.List;
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
        List<MessageEntity> incList = messageIncQueueService.getAll();
        incList.add(new MessageEntity("11", "queue1"));
        incList.add(new MessageEntity("12", "queue2"));
        modelAndView.addObject("messagesInc", incList);
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
            LOG.info("Сообщение  " + message + " отправлено в очередь " + queue + " и сохранено в базу данных, messageId = [" + messageId + "]");
        } catch (JMSException e) {
            LOG.severe("Сообщение " + message + " не отправлено в очередь " + queue + " и не сохранено в базу данных");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateTabs", method = RequestMethod.POST)
    public ModelAndView updateTabs(@RequestParam(value = "tab") String tab) {

        ModelAndView modelAndView = new ModelAndView();
        switch (tab.charAt(0)) {
            case 'o':
                modelAndView.setViewName("out");
                modelAndView.addObject("messagesOut", messageOutQueueService.getAll());
                break;
            case 'i':
                modelAndView.setViewName("inc");
                modelAndView.addObject("messagesInc", messageIncQueueService.getAll());
                break;
        }
        LOG.info("Tab [" + tab + "]");
        return modelAndView;
    }
}
