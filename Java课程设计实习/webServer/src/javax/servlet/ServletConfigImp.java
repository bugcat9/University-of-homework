package javax.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ServletConfigImp implements ServletConfig{
	
	
	Map<String,String>configuration=new HashMap<>();
	ServletContext context;

public	ServletConfigImp(Map<String,String>configuration,ServletContext context){
		this.configuration =configuration;
		this.context=context;
	}
	
	@Override
	public String getServletName() {
		// TODO Auto-generated method stub
		if(configuration.containsKey("servlet-name"))
			return configuration.get("servlet-name");
		return null;
	}


	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return context;
	}

	@Override
	public String getServletUrl() {
		// TODO Auto-generated method stub
		return configuration.get("url-pattern");
	}

	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		return configuration.get(name);
	}

}
