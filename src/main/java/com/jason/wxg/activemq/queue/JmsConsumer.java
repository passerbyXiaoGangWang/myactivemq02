package com.jason.wxg.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 消息消费者
 */
public class JmsConsumer {
    public static final String MYACTIVEMQ_URI = "tcp://47.108.61.142:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {
        //1.创建连接工厂,按照给定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(MYACTIVEMQ_URI);
        //2.通过连接工厂，获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();//启动
        //3.创建会话session
        //两个参数，第一个叫事务。第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是队列queue还是主题topic）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        //第一种消费方法，同步阻塞方式
        //订阅者或接收者调用messageConsumer.receive()来接受消息，receive在能接受到消息之前将一直阻塞
       /* while (true){
//            TextMessage textMessage = (TextMessage) messageConsumer.receive(); //不停的消费消息
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L); //4秒后不再消费
            if (null != textMessage){
                System.out.println("消费者接受到消息：" + textMessage.getText());
            } else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();*/

       //第二种消费方法，使用监听器来消费
        //通过监听的方式来消费消息 MessageConsumer messageConsumer = session.createConsumer(queue);
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("消费者接受到的消息："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();


    }
}
