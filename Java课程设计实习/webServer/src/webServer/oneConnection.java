package webServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterChainImp;
import javax.servlet.FilterConfig;
import javax.servlet.JspServletProduce;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestImp;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseImp;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionImp;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


import interbal_operation.Request;
import interbal_operation.Response;


public class oneConnection  implements Runnable{
	
	Socket sock;
	ServletContext Context;
	Request req;
	Response res;
	/**
	 * 
	 * @param sock
	 * @param Context
	 * @throws IOException
	 */
	oneConnection(Socket sock,ServletContext Context) throws IOException{
		this.sock=sock;
		this.Context=Context;
	}
	
	/**
	 * 
	 * @throws IOException	可能发生读取错误
	 * @throws ClassNotFoundException   可能找不到这个类
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public void  service() throws IOException, ClassNotFoundException, InstantiationException,
	IllegalAccessException, NoSuchMethodException, SecurityException, 
	IllegalArgumentException, InvocationTargetException {
		
		 req=new Request(sock.getInputStream(),this.Context);		
		String uri=req.getRequest();
		//		生成一个回复
		 res=new Response(sock.getOutputStream(), Context);
		
		 if(this.Context.getSession(req.getInfo().get("User-Agent"))==null)
		 {
			 HttpSession session=new HttpSessionImp();
			 this.Context.addSession(req.getInfo().get("User-Agent"), session);
		 }
		 
		 
		 if(this.Context.getFilterConfig(uri)!=null)
		 {
			 //URL[] urls=new URL[] {new URL("file:/"+"E:/Filesave/javacode/webserver/src/")};
			 URL[] urls=new URL[] {new URL("file:/"+this.Context.getContextPath()+"/")};
		     URLClassLoader loader=new URLClassLoader(urls);
		     //得到FilterConfig的配置，然后进行配置
			FilterConfig s=	this.Context.getFilterConfig(uri);
			//查找类
			 Class<?> methodClass = loader.loadClass(s.getParameter("filter-class"));
			 Filter  ft =(Filter) methodClass.newInstance();
			 HttpServletRequest httpreq=new HttpServletRequestImp(req.getMethod(),req.getInfo(),req.getData(),req.getBufferedReader(),Context);
			 HttpServletResponse httpresp=new HttpServletResponseImp(res.getOutputStream());
			 FilterChain chain=new FilterChainImp(res, uri,this.Context);
			 ft.doFilter(httpreq, httpresp, chain);
			 
		 }else
		 {
			 this.After_Filter(uri);
		 }
		 
		
		
	}
	
	public void After_Filter(String uri) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		//	   现在在welcome-file下寻找有无内,容如果没有就直接发送目录内容
			if(uri.equals("/")) {
				String filepath=this.Context.welcome_file();
				if(filepath==null) {
					res.sendDir(uri);
				}else {
					res.sendFile(filepath);
				}
			}				//对于jsp文件
			else if(uri.lastIndexOf(".jsp")!=-1) {
				
				JspServletProduce jsp=new JspServletProduce(this.Context);
				String s= jsp.writeServlet(this.Context.getResource(uri));
				System.out.println("名字是："+s);
				
			    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		        compiler.run(null, null, null, "-encoding", "UTF-8", "-cp","src/",s);
		      //  URL[] urls=new URL[] {new URL("file:/"+"E:/Filesave/javacode/webserver/src/")};
		        System.out.println("file:/"+this.Context.getContextPath());
		        URL[] urls=new URL[] {new URL("file:/"+this.Context.getContextPath()+"/")};
		        URLClassLoader loader=new URLClassLoader(urls);
		        int i=s.indexOf("/");
		        s=s.substring(i+1).replace(".java", "");
		        System.out.println(s);
		  	    Class<?> c=loader.loadClass(s);
				HttpServlet HS = (HttpServlet)c.newInstance();
				 HttpServletRequest httpreq=new HttpServletRequestImp(req.getMethod(),req.getInfo(),req.getData(),req.getBufferedReader(),Context);
				 HttpServletResponse httpresp=new HttpServletResponseImp(res.getOutputStream());
				HS.doGet(httpreq, httpresp);
			}
			else if(this.Context.getResource(uri)!=null)//判断是不是文件
			{
				res.sendFile(uri);
			}else  if(this.Context.getResourcePaths(uri)!=null)//判断是不是目录
			{
				res.sendDir(uri);
			}
			else 	//查找进行反射
			{
				ServletConfig s=	this.Context.getServletConfig(uri);
				if(s==null)
					return;
				URL[] urls=new URL[] {new URL("file:/"+this.Context.getContextPath()+"/WEB-INF/classes")};
		        URLClassLoader loader=new URLClassLoader(urls);
				Class<?> methodClass = loader.loadClass(s.getParameter("servlet-class"));
				 HttpServlet  hs=(HttpServlet) methodClass.newInstance();
				 HttpServletRequest httpreq=new HttpServletRequestImp(req.getMethod(),req.getInfo(),req.getData(),req.getBufferedReader(),Context);
				 HttpServletResponse httpresp=new HttpServletResponseImp(res.getOutputStream());
				 hs.init(s);
				  hs.doPost(httpreq, httpresp);
			}
			
	}
	
	
	
	public void run() {
		try {
			this.service();
		}catch(Exception e) {
			this.Context.log("Exception:"+e);
			System.out.println("Exception:"+e);
		}
	}
		
	
}
