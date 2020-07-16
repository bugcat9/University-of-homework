package com.Maven.SQSReciver.AwsSqsReciver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.amazonaws.services.sqs.model.Message;


public class ReciverGUI extends JFrame {
	
	JTextField jtf =null;
	JTextArea jta =null;
	SQSReciver sqsReciver = new SQSReciver();
		public ReciverGUI() {
				init();
		}
		
		void init() {
			
			this.setTitle("接收方");
			this.setLocation(300,300);
			this.setSize(500, 500);
			this.setLayout(null);
			
			//添加和队列相关的控件
			JLabel queuelabel = new JLabel("队列名字");
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
			JButton  rec_btn =new JButton("开始接收");
			this.add(rec_btn);
			rec_btn.setBounds(200, 400, 100, 30);
			rec_btn.addActionListener(new ActionListener() {
	            // 事件处理
	            public void actionPerformed(ActionEvent e) {
	            	//读取信息
	            		String Queuename =jtf.getText();
	            		 List<Message> msgs =sqsReciver.receiveMessages(Queuename);
	            		  for (Message message : msgs) {
	            			  jta.append(message.getBody()+"\n");
	          	        }
	            	
	            }
	        });
//			
//			JButton  stop_btn =new JButton("停止接收");
//			stop_btn.setBounds(300, 400, 100, 30);
//			this.add(stop_btn);
//			
			this.setVisible(true);
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		public static void main(String [] args) {
			ReciverGUI senderGUI =new ReciverGUI();
		}
}
