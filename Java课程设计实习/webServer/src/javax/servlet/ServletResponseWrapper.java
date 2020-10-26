package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;

public class ServletResponseWrapper implements ServletResponse{
	  private ServletResponse response;
	  
	  public ServletResponseWrapper(ServletResponse response) {
	        if (response == null) {
	            throw new IllegalArgumentException("Response cannot be null");
	        }
	        this.response = response;
	    }
	  
	  public ServletResponse getResponse() {
	        return this.response;
	    }
	  
	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		 return this.response.getWriter();
	}

	@Override
	public void setContentLength(int len) {
		// TODO Auto-generated method stub
		 this.response.setContentLength(len);
	}

	@Override
	public void setContentType(String type) {
		// TODO Auto-generated method stub
		this.response.setContentType(type);
	}
}
