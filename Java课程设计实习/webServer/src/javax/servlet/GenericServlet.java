package javax.servlet;

import java.io.IOException;

public abstract class GenericServlet implements Servlet ,ServletConfig{
	
	private  ServletConfig config;
	
	 public GenericServlet() {
	        // NOOP
	    }
	 
	 //区分两个init
	 public void init()
	 {
		 
	 }
	 
	 @Override
	    public void init(ServletConfig config) {
	        this.config = config;
	        this.init();
	    }
	 
	 @Override
	    public void destroy() {
	        // NOOP by default
	    }
	 
	 
	 
	 @Override
	    public abstract void service(ServletRequest req, ServletResponse res)
	            throws  IOException;
	 
	 public void log(String msg) {
	        getServletContext().log(getServletName() + ": " + msg);
	    }
	 
	 public String getServletName() {
		return null;
	}
	
	 public ServletConfig getServletConfig() {
		 return config;
	 }
	 
	 @Override
	    public ServletContext getServletContext() {
	        return getServletConfig().getServletContext();
	    }
	 
	 public String getServletUrl() {
		return this.config.getServletUrl(); 
	 }
	 
	 public String getParameter(String name) {
		 return this.config.getParameter(name);
	 }
}
