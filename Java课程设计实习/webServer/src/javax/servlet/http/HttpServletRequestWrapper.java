package javax.servlet.http;

import java.util.Enumeration;

import javax.servlet.ServletRequestWrapper;

public class HttpServletRequestWrapper extends ServletRequestWrapper implements
HttpServletRequest {

	
	public HttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	 private HttpServletRequest _getHttpServletRequest() {
	        return (HttpServletRequest) super.getRequest();
	    }
	
	
	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return this._getHttpServletRequest().getHeader(name);
	}



	@Override
	public Enumeration<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return this._getHttpServletRequest().getHeaderNames();
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this._getHttpServletRequest().getMethod();
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return this.getSession();
	}

}
