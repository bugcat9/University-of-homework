package com.Maven.Receiver.ActiveMQRecevier;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


import javax.jms.*;



public class QueueReceiver {
	String Queuename=null;		//队列名字
	ConnectionFactory connectionFactory = null;
	Connection connection = null;
	Session session = null;
	MessageConsumer consumer = null;
	TextMessage textMessage = null;
	
	String user = "user";
	String password = "user";
	String BROKEP_URL =ActiveMQConnection.DEFAULT_BROKER_URL;
	
	public QueueReceiver() {
		connectionFactory = new ActiveMQConnectionFactory(user, password, BROKEP_URL);
		
	}
	
	public void ReceiverMsg(String Queuename) {
		try {
			this.Queuename = Queuename;	
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(Queuename);
			consumer = session.createConsumer(queue);
			//设置监听器
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					message = (TextMessage) message;
					try {
						String value = ((TextMessage) message).getText();
						System.out.println("消息是："+value);
						ReceiverGUI.setMsg(value+"\n");
						
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
//			session.close();
//			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		if(session!=null) {
			try {
				session.close();
				session=null;
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(connection!=null) {
			try {
				connection.close();
				connection=null;
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		QueueReceiver queueReceiver =new QueueReceiver();
		queueReceiver.ReceiverMsg("Queue1");
	 }
	
}
