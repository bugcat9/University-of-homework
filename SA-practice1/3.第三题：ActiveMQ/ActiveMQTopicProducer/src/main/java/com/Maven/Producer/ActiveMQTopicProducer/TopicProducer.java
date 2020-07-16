package com.Maven.Producer.ActiveMQTopicProducer;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class TopicProducer {
	String Topicname=null;		//话题的名字
	ConnectionFactory connectionFactory = null;
	Connection connection = null;
	Session session = null;
	MessageProducer producer = null;
	TextMessage textMessage = null;
	
	String user = "user";
	String password = "user";
	String BROKEP_URL =ActiveMQConnection.DEFAULT_BROKER_URL;
	
	public TopicProducer() {
		connectionFactory = new ActiveMQConnectionFactory(user, password, BROKEP_URL);
		
	}
	
	public String  sendMessage(String Topicname,String msg) {
		try {
			this.Topicname = Topicname;	
			connection = connectionFactory.createConnection();
			
			session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(Topicname);
			producer = session.createProducer(topic);
			
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);		//设置消息类型
			textMessage = session.createTextMessage(msg);
			connection.start();
			
			producer.send(textMessage);
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
		TopicProducer topicProducer =new TopicProducer();
		topicProducer.sendMessage("Topic1","shou ni 223");
	 }
	
	
}
