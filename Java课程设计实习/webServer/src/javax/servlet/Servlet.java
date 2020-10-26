package javax.servlet;

import java.io.IOException;

public interface Servlet {
	
	public void init(ServletConfig config);
	
	public void service(ServletRequest req, ServletResponse res) throws IOException;
	
	//public String getServletInfo();
	
	public void destroy();
	
	//public ServletConfig getServletConfig();
	
}
