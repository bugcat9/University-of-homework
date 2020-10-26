package javax.servlet.http;

import java.io.IOException;

import javax.servlet.ServletResponse;

public interface HttpServletResponse extends ServletResponse{
	
	public void setStatus(int sc);
	
	public int getStatus();
	
	public String getHeader(String name);
	
	 public void sendRedirect(String location) throws IOException;
}
