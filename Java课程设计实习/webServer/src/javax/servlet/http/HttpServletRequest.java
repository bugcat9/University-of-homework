package javax.servlet.http;

import java.util.Enumeration;

import javax.servlet.ServletRequest;

public interface HttpServletRequest  extends ServletRequest{
	//public int getDateHeader(String name);
	
	public String getHeader(String name);
	
//	public Enumeration<String> getHeaders(String name);
	
	public Enumeration<String> getHeaderNames();
	
	 public String getMethod();
	 
	 public HttpSession getSession();
}
