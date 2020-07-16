package com.Maven.SNSSender.AwsSnsSender;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;



public class SNSSender {
	 private static  AmazonSNS amazonSNS = AmazonSNSClientBuilder.standard().withRegion("us-east-1").build();	  
	 CreateTopicResult createTopicResponse  = null;
	 public SNSSender() {
		
	 }
	 
	 public String  sendMsg(String Topic,String Msg) {
		 
			//创造一个主题
		 final CreateTopicRequest createTopicRequest = new CreateTopicRequest(Topic);
		 createTopicResponse = amazonSNS.createTopic(createTopicRequest);
		 // Print the topic ARN.
		 System.out.println("TopicArn:" + createTopicResponse.getTopicArn());
		     
		 // 打印一下id
		 System.out.println("CreateTopicRequest: " + amazonSNS.getCachedResponseMetadata(createTopicRequest));
		//发送一个消息
		 //final String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
		 final PublishRequest publishRequest = new PublishRequest(createTopicResponse.getTopicArn(), Msg);
		 final PublishResult publishResponse = amazonSNS.publish(publishRequest);

		
		 System.out.println("MessageId: " + publishResponse.getMessageId());
		 
		 return "\nMessageId: " + publishResponse.getMessageId();
	 }
	 
	 static public void main(String [] args) {
		 SNSSender snsSender =new SNSSender();
		 snsSender.sendMsg("MyTopic","test");
	 }
	 
}
