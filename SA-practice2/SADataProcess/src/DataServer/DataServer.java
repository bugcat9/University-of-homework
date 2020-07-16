package DataServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DataServer {
	private  int port =2333;		//端口
	
	public DataServer(int port) {
		this.port=port;
	}
	
	
	//开启服务器
	public void  launchServer() {
		try {
			ServerSocket ssock=new ServerSocket(port);
			System.out.println("------------------服务器端已经开启-------------------------");
			System.out.println("端口："+port);
			while(true) {
				Socket socket=ssock.accept();
				String ip=socket.getInetAddress().getHostAddress();
				System.out.println(ip+"已经连接上");
				oneconnection one = new oneconnection(socket);
				new Thread(one).start();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	public static void main(String []args) {
		DataServer dataServer =null;
		
		if(args.length==2&&args[0]=="-p") {
			int port = Integer.valueOf(args[1]);
			dataServer = new DataServer(port);
			
		}else if(args.length!=0) {
			System.out.println("Command line parameter error");
		}else {
			dataServer =new DataServer(2333);
		}
		
		if(dataServer!=null) {
			dataServer.launchServer();
		}
	}
	
}
