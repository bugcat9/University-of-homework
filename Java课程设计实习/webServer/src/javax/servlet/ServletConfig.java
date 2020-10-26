package javax.servlet;

import java.util.Enumeration;

public interface ServletConfig {
	
	 public String getServletName();
	 
	 //public String getInitParameter(String name);
	 
//	 public Enumeration<String> getInitParameterNames();
	 
	 public ServletContext getServletContext();
	 
	 public String getParameter(String name);
	 
	 public String getServletUrl();
	
}


