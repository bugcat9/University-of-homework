package javax.servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpSession;

public interface ServletContext {
	
	 public String getContextPath();	//获得工作环境的路径
	 
	 public Set<String> getResourcePaths(String path);
	 
	 public File getResource(String path);
	 
	 public InputStream getResourceAsStream(String path) throws FileNotFoundException;
	 
	 public ServletConfig getServletConfig(String url);
	 
	 public void log(String msg);

	public String welcome_file();
	
	public FilterConfig getFilterConfig(String url) ;
	
	public void addSession(String key,HttpSession session);
	
	public HttpSession getSession(String name);
}
