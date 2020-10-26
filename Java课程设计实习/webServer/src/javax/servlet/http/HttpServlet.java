package javax.servlet.http;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import interbal_operation.Method;

public abstract class HttpServlet extends GenericServlet{
	
	Method method=null;
	
	  public HttpServlet() {
	        // NOOP
	    }
	  
	  public void doGet(HttpServletRequest req, HttpServletResponse resp)
			  throws IOException
	  {
		  
	  }
	  
	 	  
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		  
	  }
	  
	  @Override
		public void service(ServletRequest req, ServletResponse res) throws IOException {
			// TODO Auto-generated method stub
		  HttpServletRequest  request;
	      HttpServletResponse response;
		  request = (HttpServletRequest) req;
          response = (HttpServletResponse) res;
          service(request,response);
		} 
	  
	  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		  this.doGet(req, resp);
	  }
	  
}
