package cn.e3mall.activeMQ;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

/**
 * Queue模式：queue模式是点对点发送消息，消息发送之后被消费者消费后将被销毁，如果没消费将保存在ActiveMQ服务器中
 * Topic模式：Topic模式是广播/订阅模式，消息发送之后不会保存在服务器，只有在消费者开启消息接收监听时才能被接收到。
 *
 * */
public class TestActiveMQ {
    /**
     * 测试queue发送消息
     * */
    @Test
    public void testQueueProducer(){
//        第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        try {
//        第二步：使用ConnectionFactory对象创建一个Connection对象。
            Connection connection = connectionFactory.createConnection();
//        第三步：开启连接，调用Connection对象的start方法。
            connection.start();
//        第四步：使用Connection对象创建一个Session对象。
            //第一个参数：是否开启事务。true：开启事务，第二个参数忽略。
            //第二个参数：当第一个参数为false时，才有意义。消息的应答模式。1、自动应答2、手动应答。一般是自动应答。
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
            Queue queue = session.createQueue("test-queue");
//        第六步：使用Session对象创建一个Producer对象。
            MessageProducer producer = session.createProducer(queue);
//        第七步：创建一个Message对象，创建一个TextMessage对象。
            TextMessage textMessage = session.createTextMessage("hello activeMQ");
//        第八步：使用Producer对象发送消息。
            producer.send(textMessage);
//        第九步：关闭资源。
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试queue接收消息
     * queue模式是点对点发送消息，消息发送之后被消费者消费后将被销毁，如果没消费将保存在ActiveMQ服务器中
     * */
    @Test
    public void testQueueConsumer(){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("test-queue");
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Topic模式：发送消息
     * */
    @Test
    public void testTopicProducer(){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("test-topic");
            MessageProducer producer = session.createProducer(topic);
            TextMessage textMessage = session.createTextMessage("hello activeMQ-topic");
            producer.send(textMessage);
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Topic模式，接收消息
     * */
    @Test
    public void testTopicConsumer(){
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.132:61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("test-topic");
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("Topic监听服务启动。。。");
            System.in.read();
            System.out.println("Topic监听服务关闭");
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
