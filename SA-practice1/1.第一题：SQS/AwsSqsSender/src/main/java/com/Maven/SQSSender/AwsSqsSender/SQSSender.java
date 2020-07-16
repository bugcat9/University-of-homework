package com.Maven.SQSSender.AwsSqsSender;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
 
import java.util.*;


public class SQSSender {
		private static AmazonSQS sqs =AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		
		private String QueueUrl =null;
		
		public SQSSender() {
		
		}
		
		
		public String sendMessage(String Queuename, String message) {
			CreateQueueRequest createQueueRequest = new CreateQueueRequest(Queuename);
			this.QueueUrl =sqs.createQueue(createQueueRequest).getQueueUrl();
			//创建队列
			String s= "Sending a message to " + QueueUrl;
	       // System.out.println("Sending a message to " + QueueUrl);
	        // 声明一个发送消息的请求
	        SendMessageRequest request = new SendMessageRequest();
	        // 指定要将消息发送到哪个队列
	        request.withQueueUrl(QueueUrl);
	        // 设置消息内容
	        request.withMessageBody(message);
	        // 发送消息
	        SendMessageResult smr=  sqs.sendMessage(request);
	        s="\n"+s+smr.toString();
	        return s;
	    }
		
		public static void main(String[] args) {
			SQSSender sender = new  SQSSender();
			System.out.println(sender.sendMessage("znn","hellow world333444"));
			
		}

}
