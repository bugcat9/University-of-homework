package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;

//接口
public interface ServletResponse {

	public PrintWriter getWriter() throws IOException;
	
	public void setContentLength(int len);
	
	public void setContentType(String type);
}
