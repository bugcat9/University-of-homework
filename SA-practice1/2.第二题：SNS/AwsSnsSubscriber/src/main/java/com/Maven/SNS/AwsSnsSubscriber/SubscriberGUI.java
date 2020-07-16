package com.Maven.SNS.AwsSnsSubscriber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;


public class SubscriberGUI extends JFrame {
	JTextField jtfTopic =null;
	JTextField jtfedpoint=null;
	String type ="email";
	
	SNSSubscriber snsSubscriber =new SNSSubscriber();
			public SubscriberGUI() {
				init();
			}
			
			void init() {
				this.setTitle("订阅方");
				this.setLocation(300,300);
				this.setSize(500, 500);
				this.setLayout(null);
			
		        // 需要选择的条目
		        String[] listData = new String[]{"邮件", "SQS"};
				 // 创建一个下拉列表框
		        final JComboBox<String> comboBox = new JComboBox<String>(listData);
		        
		        // 添加条目选中状态改变的监听器
		        comboBox.addItemListener(new ItemListener() {

		            public void itemStateChanged(ItemEvent e) {
		                // 只处理选中的状态
		                if (e.getStateChange() == ItemEvent.SELECTED) {
//		                    type=comboBox.getSelectedItem().toString();
		                	if(comboBox.getSelectedIndex()==0) {
		                		type= "email";
		                	}else if (comboBox.getSelectedIndex()==1) {
								type="SQS";
							}
		                	System.out.println("选中: " + comboBox.getSelectedIndex() + " = " + comboBox.getSelectedItem());
		                }
		            }
		        });
				
		        this.add(comboBox);
		        comboBox.setBounds(80,10,400,30);
		        
		        JLabel jbl =new JLabel("订阅方式:");
		        this.add(jbl);
		        jbl.setBounds(0,0,60,40);
		        
		        
		    	//添加相关的控件
				JLabel ponitlabel = new JLabel("endPoint");
				this.add(ponitlabel);
				ponitlabel.setBounds(0,210,70,20);
				 jtfedpoint =new JTextField();
				this.add(jtfedpoint);
				jtfedpoint.setBounds(80, 210, 400, 30);
				
				//添加相关的控件
				JLabel Topiclabel = new JLabel("TpoicName");
				this.add(Topiclabel);
				Topiclabel.setBounds(0,110,70,20);
				 jtfTopic =new JTextField();
				this.add(jtfTopic);
				jtfTopic.setBounds(80, 110, 400, 30);
		        
				//添加按钮
				JButton  btn =new JButton("订阅");
				this.add(btn);
				btn.setBounds(200, 400, 100, 30);
				btn.addActionListener(new ActionListener() {
		            // 事件处理
		            public void actionPerformed(ActionEvent e) {
		            	String topic = jtfTopic.getText();
		            	String endpoint = jtfedpoint.getText();
		            	System.out.println("Topic:"+topic);
		            	System.out.println("endpoint:"+endpoint);
		            	boolean isok=snsSubscriber.setSubscriber("MyTopic","email","1767508581@qq.com");
		            	if(isok) {
		            		 JOptionPane.showMessageDialog(null, "订阅成功", "提醒", JOptionPane.PLAIN_MESSAGE);
		            	}else {
		            		 JOptionPane.showMessageDialog(null, "订阅失败", "提醒", JOptionPane.PLAIN_MESSAGE);
		            	}
		            }
		        });
		        
				this.setVisible(true);
				this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
			
			
			public static void main(String [] args) {
				SubscriberGUI senderGUI =new SubscriberGUI();
			}

}
