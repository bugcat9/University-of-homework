package javax.servlet;

import java.io.BufferedReader;
import java.util.Enumeration;

public interface ServletRequest {
	
	public void setAttribute(String name, Object o);
	
	public Object getAttribute(String name);
	
	//public Enumeration<String> getAttributeNames();
	
	public int getContentLength();//返回请求主体的字节数
	
	 public String getContentType();//返回主体的MIME类型
	 
	 public String getParameter(String name);//返回请求参数的值
	 
	 public Enumeration<String> getParameterNames();
	 
	 public BufferedReader getReader();
	 
	 public RequestDispatcher getRequestDispatcher(String path);
	 
	 public ServletContext getServletContext();
}
