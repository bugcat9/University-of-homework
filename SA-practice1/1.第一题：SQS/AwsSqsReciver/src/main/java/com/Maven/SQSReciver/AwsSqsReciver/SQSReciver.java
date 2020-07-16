package com.Maven.SQSReciver.AwsSqsReciver;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
 
import java.util.*;

public class SQSReciver {
	
	private static AmazonSQS sqs =AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
	
	private String QueueUrl =null;
	
	public SQSReciver() {
	
	}
	
	 public   List<Message> receiveMessages(String Queuename) {
		 
			CreateQueueRequest createQueueRequest = new CreateQueueRequest(Queuename);
			this.QueueUrl =sqs.createQueue(createQueueRequest).getQueueUrl();
		//	String s="Receiving messages from " + QueueUrl;
			//创建队列
	        System.out.println("Receiving messages from " + QueueUrl);
	        // 声明一个接收消息的请求
	        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(QueueUrl);
	        // 设置一些参数
	        receiveMessageRequest.setMaxNumberOfMessages(5);
	        receiveMessageRequest.withWaitTimeSeconds(10);
	        // 声明一个存放消息的List
	        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	        // 遍历List，打印消息内容
	        for (Message message : messages) {
	            System.out.println("Message: " + message.getBody());
	            // 删除已被接收的消息
	            System.out.println("Deleting a message.");
	            sqs.deleteMessage(QueueUrl, message.getReceiptHandle());
	        }
	        return  messages;
	    }
	 
	 public static void main(String[] args) {
		 SQSReciver sender = new  SQSReciver();
			sender.receiveMessages("znn");
		}
}
