package javax.servlet.http;

import java.io.IOException;

import javax.servlet.ServletResponseWrapper;

import interbal_operation.Status;

public class HttpServletResponseWrapper extends ServletResponseWrapper
implements HttpServletResponse {
	
	Status sc=null;
	public HttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
	}

	  private HttpServletResponse _getHttpServletResponse() {
	        return (HttpServletResponse) super.getResponse();
	    }
	  
	@Override
	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		this._getHttpServletResponse().setStatus(sc);
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return this._getHttpServletResponse().getStatus();
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		 return this._getHttpServletResponse().getHeader(name);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		this._getHttpServletResponse().sendRedirect(location);
	}

}
