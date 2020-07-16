package com.Maven.Producer.ActiveMQTopicProducer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ProducerGUI extends JFrame{
	JTextField jtf =null;
	JTextArea jta =null;
	TopicProducer topicProducer =new TopicProducer();
		public ProducerGUI() {
				init();
		}
		
		void init() {
			
			this.setTitle("生产者");
			this.setLocation(300,300);
			this.setSize(500, 500);
			this.setLayout(null);
			
			//添加和队列相关的控件
			JLabel queuelabel = new JLabel("主题名字");
			this.add(queuelabel);
			queuelabel.setBounds(0,10,70,20);
			 jtf =new JTextField();
			this.add(jtf);
			jtf.setBounds(80, 10, 400, 30);
			
			//添加发送消息相关的控件
			JLabel MsgLabel = new JLabel("消息:");
			this.add(MsgLabel);
			MsgLabel.setBounds(0, 80, 70, 20);
			
			jta =new JTextArea(10,10);
			jta.setLineWrap(true);
			JScrollPane jp=new JScrollPane();
			jp.setViewportView(jta);
			jp.setBounds(40, 90, 400, 300);
			this.add(jp);
			
			
			//添加按钮
			JButton  btn =new JButton("发送");
			this.add(btn);
			btn.setBounds(200, 400, 100, 30);
			btn.addActionListener(new ActionListener() {
	            // 事件处理
	            public void actionPerformed(ActionEvent e) {

	            		System.out.println(jtf.getText());
	            		System.out.println(jta.getText());
	            		String Topic = jtf.getText();
	            		String msg = jta.getText();
	            		String s=	topicProducer.sendMessage(Topic,msg);
	            		jta.append(s);
//	            		jta.setText(null);
//	            		//使用消息提示框输出信息
//	                    JOptionPane.showMessageDialog(null, "You input is ", "111", JOptionPane.PLAIN_MESSAGE);
	            }
	        });
			
			
			this.setVisible(true);
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		public static void main(String [] args) {
			ProducerGUI producerGUI =new ProducerGUI();
		}
}
