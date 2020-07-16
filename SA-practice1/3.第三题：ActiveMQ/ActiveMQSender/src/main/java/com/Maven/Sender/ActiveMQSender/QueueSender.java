package com.Maven.Sender.ActiveMQSender;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class QueueSender {
		String Queuename=null;		//队列名字
		
		Connection connection = null;
		Session session = null;
		MessageProducer producter = null;
		TextMessage textMessage = null;
		
		String user = "user";
		String password = "user";
		String BROKEP_URL =ActiveMQConnection.DEFAULT_BROKER_URL;
		ConnectionFactory connectionFactory =null;
		
		public QueueSender() {
			 connectionFactory = new ActiveMQConnectionFactory(user, password, BROKEP_URL);
		}
		
		public String sendMessage(String Queuename,String msg) {
			try {
				this.Queuename = Queuename;	
				connection = connectionFactory.createConnection();
				connection.start();
				session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
				Queue queue = session.createQueue(Queuename);
				producter = session.createProducer(queue);
				
				producter.setDeliveryMode(DeliveryMode.NON_PERSISTENT);		//设置消息类型
				textMessage = session.createTextMessage(msg);
				
				producter.send(textMessage);
				System.out.println("发送成功");
			
				session.close();
				connection.close();
				
				return "\n发送成功";
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		 public static void main(String[] args) throws Exception {
			 QueueSender queueSender =new QueueSender();
			 queueSender.sendMessage("Queue1","hellow world3");
		 }
}
