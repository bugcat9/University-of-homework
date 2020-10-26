package javax.servlet;

import java.io.BufferedReader;
import java.util.Enumeration;

public class ServletRequestWrapper implements ServletRequest{
	 private ServletRequest request;

	public ServletRequestWrapper(ServletRequest request) {
		// TODO Auto-generated constructor stub
		 if (request == null) {
	            throw new IllegalArgumentException("Request cannot be null");
	        }
	        this.request = request;
	}
	
	  public ServletRequest getRequest() {
	        return this.request;
	    }

	

	@Override
	public int getContentLength() {
		// TODO Auto-generated method stub
		return this.request.getContentLength();
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return this.request.getContentType();
	}

	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		return this.request.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return this.request.getParameterNames();
	}

	@Override
	public BufferedReader getReader() {
		// TODO Auto-generated method stub
		 return this.request.getReader();
	}

	@Override
	public void setAttribute(String name, Object o) {
		// TODO Auto-generated method stub
		this.request.setAttribute(name, o);
	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return this.request.getAttribute(name);
	}

	 
	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		return this.request.getRequestDispatcher(path);
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.request.getServletContext();
	}
}
