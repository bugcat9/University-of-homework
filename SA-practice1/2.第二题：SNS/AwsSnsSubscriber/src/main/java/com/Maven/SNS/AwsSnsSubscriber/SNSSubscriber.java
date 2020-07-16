package com.Maven.SNS.AwsSnsSubscriber;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;


public class SNSSubscriber {
	 private  AmazonSNS amazonSNS = AmazonSNSClientBuilder.standard().withRegion("us-east-1").build();	  ;
	 CreateTopicResult createTopicResponse  = null;
	 public SNSSubscriber() {
  		 
	 }
	 
	 /**
	  * 
	  * @param Topic 主题
	  * @param type 类型
	  * @param endpiont 终点
	  * @return
	  */
	 public  boolean setSubscriber(String Topic,String type,String endpiont) {
		
		 final CreateTopicRequest createTopicRequest = new CreateTopicRequest(Topic);
		 createTopicResponse = amazonSNS.createTopic(createTopicRequest);
		  SubscribeRequest subscribeRequest =null;
		 if(type=="email") {
			 subscribeRequest = new SubscribeRequest(createTopicResponse.getTopicArn(), "email", endpiont);
		 }else if(type=="SQS") {
		 subscribeRequest = new SubscribeRequest(createTopicResponse.getTopicArn(), "SQS",endpiont);
		 }else {
			 return false;
		 }
		
	
		 amazonSNS.subscribe(subscribeRequest);


		 System.out.println("SubscribeRequest: " + amazonSNS.getCachedResponseMetadata(subscribeRequest));
		 System.out.println("To confirm the subscription, check your email.");
		 return true;
		 
	 }
		static public void main(String []args) {
			SNSSubscriber snsSubscriber =new SNSSubscriber();
			snsSubscriber.setSubscriber("MyTopic","email","1767508581@qq.com");
		}
}
