package cn.e3mall.activeMQ;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class TestActiveMQ_Spring {


    /**
     * 整合spring发送消息
     * */
    @Test
    public void testQueueProducter(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //获得模版类对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //获得目的地对象
        Destination destination = (Destination) applicationContext.getBean("queueDestination");
        //发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage("hello spring ActiveMQ");
                return message;
            }
        });
    }


}
