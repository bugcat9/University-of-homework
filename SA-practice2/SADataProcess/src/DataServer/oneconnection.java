package DataServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class oneconnection implements Runnable{
			
			Socket socket;
			BufferedReader reader;					//读
			PrintWriter writer;								//写
			SqlConnection sqlConnection;		//数据库连接
			public oneconnection(Socket socket) {
				this.socket = socket;
				sqlConnection =new SqlConnection();
				try {
					reader =new BufferedReader( new InputStreamReader(socket.getInputStream()));
					writer = new PrintWriter(socket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			//开始等待接受客户端消息
			//开始oneconnection的接收请求，然后做出相应的处理
			private void start() {
				String str = null;
				
				try {
					while((str=reader.readLine())!=null) {
								System.out.println(str);
								String[] args = str.split("#");
								int InquiryDate = Integer.valueOf(args[1]);
								String res=null;
								if(str.startsWith("01")) {	
									//看具体某一天

									res="01#"+sqlConnection.Select_day(InquiryDate-1,args[2]);
									
								}else if(str.startsWith("02")) {
									//查看具体某一个月

									res="02#"+sqlConnection.Select_month(InquiryDate-1,args[2]);
									
								}else if(str.startsWith("03")) {
									//查看一个月的平均值
									String result =sqlConnection.Select_month(InquiryDate-1,args[2]);
									String []data = result.split(",");
									float sum=0;
									
									 for (int i = 0 ; i < data.length-1 ; i++){
										 sum+=Float.valueOf(data[i]);
									 }
									sum =sum/data.length-1;
										
									res = "03#"+sum;
								}else if(str.startsWith("04")) {
									//查看具体某一个年
									
								}
								SendMsg(res);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			/**
			 * 发送消息
			 * @param msg
			 */
		void SendMsg(String msg) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					System.out.println("send："+msg);
					writer.println(msg);
					writer.flush();
				}
			}).start();
		}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				this.start();
			}
		
		/**
		 * 查询季度
		 * @param date
		 * @return
		 */
		
}
