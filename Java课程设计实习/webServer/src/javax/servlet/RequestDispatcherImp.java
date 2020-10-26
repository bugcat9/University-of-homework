package javax.servlet;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;



public class RequestDispatcherImp implements RequestDispatcher{

	ServletContext Context;
	String uri;
	/**
	 * 实现转发和协助
	 * @param path
	 * @param Context
	 */
	public	RequestDispatcherImp(String path,ServletContext Context) {
		this.Context=Context;
		this.uri=path;
	}
	
	
	
	public void forward(ServletRequest request, ServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		try {
			
		if(uri.lastIndexOf(".jsp")!=-1) {
			
			JspServletProduce jsp=new JspServletProduce(this.Context);
			String s= jsp.writeServlet(this.Context.getResource("/"+uri));
		    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	        compiler.run(null, null, null, "-encoding", "UTF-8", "-cp","src/",s);
	        URL[] urls=new URL[] {new URL("file:/"+this.Context.getContextPath()+"/")};
	        URLClassLoader loader=new URLClassLoader(urls);
	        
	        int i=s.indexOf("/");
	        s=s.substring(i+1).replace(".java", "");
	        Class<?> c= loader.loadClass(s);
	    	HttpServlet HS = (HttpServlet)c.newInstance();
			HS.doGet((HttpServletRequest)request, (HttpServletResponse)response);
			} 
		
		}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void include(ServletRequest request, ServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
