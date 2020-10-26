package javax.servlet;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import guiServer.ServerController;



public class ServletContextImp  implements  ServletContext{
	
	StringBuilder logfile=new StringBuilder();
	String ContextPath="/WebContent";
	String filepath;
	ArrayList<String> welcome_file_list=new ArrayList<String>();
	
	Set<ServletConfig> servletConfigSet=new HashSet<>();//对于servletConfig的集合
	
	Set<FilterConfig>filterConfigSet=new HashSet<>();//对于filterConfig的集合
	
	Map<String,HttpSession>httpSession=new HashMap<>();
	/**
	 * 
	 * @param path 当前工作环境
	 * @throws DocumentException		读取xml时可能抛出异常
	 */
	public ServletContextImp(String path) throws DocumentException{
		filepath=path;
		String url=filepath+"//WEB-INF//web.xml";//记得更改
		System.out.println(url);
		ReadXML(url);//读配置文件
	}
	
	/**
	 * 返回路径下的资源
	 */
	@Override
	public Set<String> getResourcePaths(String path) {
		// TODO Auto-generated method stub
		if(!path.startsWith("/"))
			return null;
		Set<String> set=new HashSet<>();
		File f=new File(this.filepath+path);
		if(!f.exists()||f.isFile())
			return null;
		File[] files	=f.listFiles();
		for(File file:files) {
			if(file.isDirectory()) {
				
				set.add(path+file.getName()+"/");
				
			}else if(file.isFile()) {
				set.add(path+file.getName());
			}
				
		}
		return set;
	}

	@Override
	public File getResource(String path) {
		// TODO Auto-generated method stub
		File f=new File(this.filepath+path);
		if(f.exists()&&f.isFile())
			return f;
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) throws FileNotFoundException {
		// TODO Auto-generated method stub
		InputStream in =new FileInputStream(new File(path));
		return null;
	}

	
	@Override
	public void log(String msg) {
		// TODO Auto-generated method stub
		ServerController.log(msg);
		logfile.append(msg);
	}


	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return this.filepath;
	}
	
	public ServletConfig getServletConfig(String url) {
		for(ServletConfig s:this.servletConfigSet) {
			if(s.getServletUrl().equals(url))
				return s;
		}
		return null;
	}
	
	public FilterConfig getFilterConfig(String url) {
		for(FilterConfig s:this.filterConfigSet) {
			if(url.matches(s.getUrl()))
				return s;
		}
		return null;
	}
	
	
	
	//读取配置文件
	 void ReadXML(String  url) throws DocumentException {
			SAXReader reader = new SAXReader();
		    Document document = reader.read(url);	//读url
		    Element root = document.getRootElement();
		    
		    Iterator<Element> it = root.elementIterator();
		    
		    ServletConfig servletconfig=null;		//新的ServletConfig
			Map<String,String>servletconfiguration=null;	
			
			FilterConfig filterconfig=null; //新的FilterConfig
			Map<String,String>filterconfiguration=null;
		    while(it.hasNext()) 
		    {
		    	Element ele = it.next();
	    		//System.out.println(ele.getName());
	    		 if(ele.getName().equals("welcome-file-list")) {
		 			 Iterator<Element> it1=ele.elementIterator();
		    		while(it1.hasNext()) {
			    		Element e = it1.next();
			    		this.welcome_file_list.add(e.getText());
		    		}
	    		}
	    		 	
	    		 //对于servlet
	    			if(ele.getName().equals("servlet")) {
	    				servletconfiguration=new HashMap<>();
			    		Iterator<Element> it1=ele.elementIterator();
			    		while(it1.hasNext()) {
				    		Element e = it1.next();
				    		servletconfiguration.put(e.getName(), e.getText());
				    	}
			    	}
	    			//对于servlet-mapping
	    			if(ele.getName().equals("servlet-mapping")) {
			    		
			    		Iterator<Element> it1=ele.elementIterator();
			    		while(it1.hasNext()) {
				    		Element e = it1.next();
				    		servletconfiguration.put(e.getName(), e.getText());
				    	}
			    		servletconfig=new ServletConfigImp(servletconfiguration, this);
			    		this.servletConfigSet.add(servletconfig);
			    		servletconfig=null;
			    		servletconfiguration=null;
			    	}
	    			
	    			 //对于filter
	    			if(ele.getName().equals("filter")) {
	    				filterconfiguration=new HashMap<>();
			    		Iterator<Element> it1=ele.elementIterator();

			    		while(it1.hasNext()) {
				    		Element e = it1.next();
				    		filterconfiguration.put(e.getName(), e.getText());
				    	}
			    	}
	    			//对于filter-mapping
	    			if(ele.getName().equals("filter-mapping")) {
			    		
			    		Iterator<Element> it1=ele.elementIterator();
			    		while(it1.hasNext()) {
				    		Element e = it1.next();
				    		filterconfiguration.put(e.getName(), e.getText());
				    	}
			    		filterconfig=new FilterConfigImp(filterconfiguration, this);
			    		this.filterConfigSet.add(filterconfig);
			    		filterconfig=null;
			    		filterconfiguration=null;
			    	}
	    			
		    }
		 
	 }
		    		
	public void addSession(String key,HttpSession session)
	{
		this.httpSession.put(key, session);
	}
	 
	 public String welcome_file() {
		 for(String s:welcome_file_list) {
			 File f=this.getResource("/"+s);
			 if(f!=null&&f.exists())
				 return "/"+s;
		 }
		 return null;
	 }
	 
	 public static void main(String []agrs) throws DocumentException {
		 ServletContext s=new ServletContextImp("WebContent");
		 
	 }

	@Override
	public HttpSession getSession(String name) {
		// TODO Auto-generated method stub
		return this.httpSession.get(name);
	}
}
