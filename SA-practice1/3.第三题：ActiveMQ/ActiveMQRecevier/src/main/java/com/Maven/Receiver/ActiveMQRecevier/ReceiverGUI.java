package com.Maven.Receiver.ActiveMQRecevier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class ReceiverGUI extends JFrame {

	JTextField jtf =null;
	QueueReceiver queueReceiver =new QueueReceiver();
	static JTextArea jta =new JTextArea(10,10);
		public ReceiverGUI() {
				init();
		}
		
		public static void setMsg(String msg) {
			jta.append(msg);
		}
		
		void init() {
			
			this.setTitle("接收方");
			this.setLocation(300,300);
			this.setSize(500, 500);
			this.setLayout(null);
			 
			
			jta.setLineWrap(true);
			JScrollPane jp=new JScrollPane();
			jp.setViewportView(jta);
			jp.setBounds(40, 90, 400, 300);
			this.add(jp);
			
			
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
			
			
			//添加按钮
			JButton  rec_btn =new JButton("开始接收");
			this.add(rec_btn);
			rec_btn.setBounds(200, 400, 100, 30);
			rec_btn.addActionListener(new ActionListener() {
	            // 事件处理
	            public void actionPerformed(ActionEvent e) {

	            		System.out.println(jtf.getText());
	            		queueReceiver.ReceiverMsg(jtf.getText());
	            		//System.out.println(jta.getText());
	            		//jta.setText(null);
	            		//使用消息提示框输出信息
	                  //  JOptionPane.showMessageDialog(null, "You input is ", "111", JOptionPane.PLAIN_MESSAGE);
	            }
	        });
			
//			JButton  stop_btn =new JButton("停止接收");
//			stop_btn.setBounds(300, 400, 100, 30);
//			this.add(stop_btn);
//			stop_btn.addActionListener(new ActionListener() {
//	            // 事件处理
//	            public void actionPerformed(ActionEvent e) {
//	            	queueReceiver.close();
//	            	JOptionPane.showMessageDialog(null, "停止成功", "提醒", JOptionPane.PLAIN_MESSAGE);
//	            }
//	        });
			
			
			this.setVisible(true);
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		}
		
		public static void main(String [] args) {
			ReceiverGUI receviGui =new ReceiverGUI();
		}
}
