package com.Maven.Consumer.ActiveMQTopicConsumer;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


import javax.jms.*;


public class TopicConsumer {
	String Topicname=null;		//话题的名字
	
	String clientID = null;
	ConnectionFactory connectionFactory = null;
	Connection connection = null;
	Session session = null;
	MessageConsumer consumer =null;
	TextMessage textMessage = null;
	
	String user = "user";
	String password = "user";
	String BROKEP_URL =ActiveMQConnection.DEFAULT_BROKER_URL;
	
	public TopicConsumer() {
		connectionFactory = new ActiveMQConnectionFactory(user, password, BROKEP_URL);
	}
	
	public void ReciveMsg(String Topicname,String clientID) {
		try {
			this.Topicname = Topicname;	
			this.clientID =clientID;
			connection = connectionFactory.createConnection();
			connection.setClientID(clientID);
			
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(Topicname);
			consumer = session.createDurableSubscriber(topic, clientID);
			connection.start();
			
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					message = (TextMessage) message;
					try {
						String value = ((TextMessage) message).getText();
						System.out.println( value);
						ConsumerGUI.setMsg(value);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
//			session.close();
//			session = null;
//			connection.close();
//			connection = null;
			
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
		TopicConsumer topicConsumer =new TopicConsumer();
		topicConsumer.ReciveMsg("MyTopic","znn");
		
	 }
	
}
