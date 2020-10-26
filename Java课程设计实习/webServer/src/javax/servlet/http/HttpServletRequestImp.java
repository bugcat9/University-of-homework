package javax.servlet.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.RequestDispatcherImp;
import javax.servlet.ServletContext;

import interbal_operation.Method;

public class HttpServletRequestImp implements HttpServletRequest {
	
	Method HeadMethod;
	Map<String,String>Info;
	StringBuilder reqData;
	Map<String,String>Parameter=new HashMap<>();
	BufferedReader input;
	ServletContext Context=null;
	Map<String,Object>Attribute=new HashMap<>();
	
	public	HttpServletRequestImp(Method HeadMethod,Map<String,String>Info,
			StringBuilder reqData,BufferedReader input,ServletContext Context)
			throws UnsupportedEncodingException{
		this.HeadMethod=HeadMethod;
		this.Info=Info;
		this.reqData=reqData;
		if(reqData.length()!=0)
			this.ParseParameter(reqData.toString());
		this.input=input;
		this.Context=Context;
	}
	
	

	@Override
	public int getContentLength() {
		// TODO Auto-generated method stub
		if(Info.containsKey("Content-Length"))
			return  Integer.valueOf( Info.get("Content-Length"));
		return 0;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		if(Info.containsKey("Content-Type"))
			return Info.get("Content-Type");
		return null;
	}

	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		if(this.Parameter.containsKey(name))
			return this.Parameter.get(name);
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return Collections.enumeration(this.Parameter.keySet());
	}

	@Override
	public BufferedReader getReader() {
		// TODO Auto-generated method stub
		return this.input;
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		if(this.Info.containsKey(name))
			return Info.get(name);
		return null;
	}

	
	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return Collections.enumeration(this.Info.keySet());
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		
		return HeadMethod.toString();
	}
	
	/**
	 * 解析请求体
	 * @param s 请求体
	 * @throws UnsupportedEncodingException
	 */
	  void  ParseParameter(String s) throws UnsupportedEncodingException {
		
		//	String s="username=123&password=1111&age=222222222222&sex=%E7%94%B7";
		String type= Info.get("Content-Type");
		if(type.indexOf("application/x-www-form-urlencoded")>-1) {
			//采用UTF-8字符集进行解码
	        System.out.println(URLDecoder.decode(s, "UTF-8"));
	         s=URLDecoder.decode(s, "UTF-8");
	        String []a=s.split("&");
	        for(String str:a) {
	        	System.out.println(str);
	        	String []b=str.split("=");
	        	Parameter.put(b[0], b[1]);
	        }
		}      
	}



	@Override
	public void setAttribute(String name, Object o) {
		// TODO Auto-generated method stub
		this.Attribute.put(name,o);
	}



	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return this.Attribute.get(name);
	}



	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		RequestDispatcher  requestDispatcher=new RequestDispatcherImp(path,Context);
		return requestDispatcher;
	}



	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.Context;
	}



	//实现
	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return this.Context.getSession(this.Info.get("User-Agent"));
	}
	
	
}
