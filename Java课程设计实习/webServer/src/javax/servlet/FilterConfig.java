package javax.servlet;

public interface FilterConfig {

	 public String getFilterName();
	 
	 public ServletContext getServletContext();
	 
	 public String getUrl() ;//自己加的接口
	 
	 public String  getParameter(String name);
	 
	// public String getInitParameter(String name);
	 
	// public Enumeration<String> getInitParameterNames();
	
}
