package guiServer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import org.dom4j.DocumentException;

import webServer.WebServer;

public class ServerController extends JFrame{
	String path;
	static JTextArea jt=new JTextArea(10,10);
	
	JTextField jtport;
	public  ServerController() {
		
	}
	
	   void launch() {
		   this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		   
		   this.setTitle("webserver");
		   this.setSize(800, 500);
		   
		   //设置布局
		   this.setLayout(new BorderLayout(10,5));
		   //得到上半部界面
		   JPanel p1=this.addPanel();
		   this.add(p1,BorderLayout.NORTH);
		   p1.setBorder(BorderFactory.createTitledBorder("选择"));
		   
		   //得到下半部界面
		   JScrollPane p2=this.addTextPanel();
		   this.add(p2,BorderLayout.CENTER);
		 
		   p2.setBorder(BorderFactory.createTitledBorder("日志"));
		   jt.setLineWrap(true);
		   this.setVisible(true);
	   }
	   
	   
	   public JPanel addPanel() {
		   JPanel jp=new JPanel();//第一个面
		  
		   GridBagLayout gridBagLayout = new GridBagLayout();
		   jp.setLayout(gridBagLayout);
		   GridBagConstraints constraints = new  GridBagConstraints();
		   
		   //选择主目录按钮
		   JButton ChoseBtu=new JButton();
		   ChoseBtu.setText("选择主目录");
		   constraints.weightx = 0;constraints.weighty = 0;
		   constraints.gridheight = 1;constraints.gridwidth =1;
		   constraints.fill=GridBagConstraints.BOTH;
		   gridBagLayout.setConstraints(ChoseBtu, constraints);
		   jp.add(ChoseBtu);
		 
		   
		   
		   //webcontent,目录
		   JLabel label=new JLabel();
		   label.setText("webContent:");
		  constraints.weightx = 0;constraints.weighty = 0;
		   constraints.gridheight = 1;constraints.gridwidth =1;
		   gridBagLayout.setConstraints(label, constraints);
		   jp.add(label);
		   
		  JTextField jt=new JTextField(20);
		  constraints.weightx = 0.0;constraints.weighty = 0.0;
		   constraints.gridheight = 1;constraints.gridwidth =5;
		   gridBagLayout.setConstraints(jt, constraints);
		   jp.add(jt);
		  
		  
		  //端口的选择
		  JLabel label2=new JLabel();
		  label2.setText("port:");
		 constraints.weightx = 0.0;constraints.weighty = 0.0;
		  constraints.gridheight = 1;constraints.gridwidth =3;
		  gridBagLayout.setConstraints(label2, constraints);
		  jp.add(label2);
		  
		   jtport=new JTextField("80", 5);
	
		  constraints.gridheight = 3;constraints.gridwidth =3;
		  gridBagLayout.setConstraints(jtport, constraints);
		  jp.add(jtport);
		  
		  JButton Startbtu= new JButton("启动");
		  jp.add(Startbtu);
		  
		  //选择按钮添加事件
		  ChoseBtu.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	 JFileChooser jfc=new JFileChooser();  
	                 jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
	                 jfc.showDialog(new JLabel(), "选择");  
	                 File file=jfc.getSelectedFile();  
	                 if(file.isDirectory()){  
	                     System.out.println("文件夹:"+file.getAbsolutePath());  
	                 }else if(file.isFile()){  
	                     System.out.println("文件:"+file.getAbsolutePath());  
	                 }  
	                 System.out.println(jfc.getSelectedFile().getName());  
	                 path=file.getAbsolutePath();
	        		jt.setText(file.getAbsolutePath());
	            }
	            }
	            );
		  
		  //先实现一个简单的方法后面有时间就填坑
		  Startbtu.addActionListener(new ActionListener(){
			   @Override
	            public void actionPerformed(ActionEvent e) {
				   int port=Integer.valueOf( jtport.getText());

				   try {
					   System.out.println("开启服务器	"+port+"	"+path);
					   //创建一个服务器
					   WebServer webServer=new WebServer(port, path);
					   new Thread(webServer).start();		//服务器开启
					} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			   }
			   
		  });
		  
		  
		  
		  return jp;
	   }
	   
	   public  JScrollPane addTextPanel() {
		   JScrollPane jp=new JScrollPane();
			jp.setViewportView(jt);
			 return jp;
	   }
	   
	
	   public static void  log(String str)
	   {
		   jt.append(str);
	   }
	   
	   
	 public static void main(String[] args) {
		 ServerController sc=new ServerController();
		 sc.launch();
	 }
	
}
