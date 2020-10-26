package javax.servlet;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import interbal_operation.Response;

public class FilterChainImp implements FilterChain{
	
	Response res;
	String	uri;
	ServletContext Context;
	/**
	 * 
	 * @param res
	 */
	public FilterChainImp(Response res,String uri,ServletContext Context) {
		this.res=res;
		this.uri=uri;
		this.Context=Context;
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response) throws IOException {
		// TODO Auto-generated method stub
//		   现在在welcome-file下寻找有无内,容如果没有就直接发送目录内容
				if(uri.equals("/")) {
					String filepath=this.Context.welcome_file();
					if(filepath==null) {
						res.sendDir(uri);
					}else {
						res.sendFile(filepath);
					}
				}				//对于jsp文件
				else if(uri.lastIndexOf(".jsp")!=-1) {
					
					JspServletProduce jsp=new JspServletProduce(this.Context);
					String s= jsp.writeServlet(this.Context.getResource(uri));
					System.out.println("名字是："+s);
					
				    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			        compiler.run(null, null, null, "-encoding", "UTF-8", "-cp","src/",s);
			      //  URL[] urls=new URL[] {new URL("file:/"+"E:/Filesave/javacode/webserver/src/")};
			        System.out.println("file:/"+this.Context.getContextPath());
			        URL[] urls=new URL[] {new URL("file:/"+this.Context.getContextPath()+"/")};
			        URLClassLoader loader=new URLClassLoader(urls);
			        int i=s.indexOf("/");
			        s=s.substring(i+1).replace(".java", "");
			        System.out.println(s);
			  	    
					try {
						 Class<?> c = loader.loadClass(s);
						HttpServlet HS= (HttpServlet)c.newInstance();	
						HS.doGet((HttpServletRequest)request, (HttpServletResponse)response);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(this.Context.getResource(uri)!=null)//判断是不是文件
				{
					res.sendFile(uri);
				}else  if(this.Context.getResourcePaths(uri)!=null)//判断是不是目录
				{
					res.sendDir(uri);
				}
				else 	//查找进行反射
				{
					ServletConfig s=	this.Context.getServletConfig(uri);
					if(s==null)
						return;
					URL[] urls=new URL[] {new URL("file:/"+this.Context.getContextPath()+"/WEB-INF/classes")};
			        URLClassLoader loader=new URLClassLoader(urls);
					
					try {
						Class<?> methodClass = loader.loadClass(s.getParameter("servlet-class"));
						HttpServlet  hs=(HttpServlet) methodClass.newInstance();
						 hs.init(s);
						 hs.doGet((HttpServletRequest)request, (HttpServletResponse)response);
						 
					} catch (ClassNotFoundException e) {
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
			}	
		
		
	

}
