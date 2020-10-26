package webServer;
import java.io.*;
import java.net.*;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextImp;

import org.dom4j.DocumentException;


import interbal_operation.ParseXML;



public class WebServer implements Runnable {
	final int httpd;
	ServletContext Context;
	public WebServer(int port,String path) throws DocumentException  {
		httpd=port;
		Context=new ServletContextImp(path);
	}
	
	//开启服务器
	public void startWeb() throws IOException
	{
		ServerSocket ssock=new ServerSocket(httpd);
		String msg="have opened port 80 locally";
		System.out.println("have opened port 80 locally");
		this.Context.log(msg+"\r\n");
			while(true)
			{
				Socket sock=ssock.accept();
				System.out.println("---------------------------------------");
				System.out.println("client has made socket connection");
				oneConnection client=new oneConnection(sock,Context);
				new Thread(client).start();
			}
	}
	

	public static void main(String[] args) throws IOException, DocumentException {
		WebServer s=new WebServer(80,"WebContent");
		s.startWeb();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.startWeb();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
