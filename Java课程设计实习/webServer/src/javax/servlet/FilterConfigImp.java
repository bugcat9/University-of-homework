package javax.servlet;

import java.util.HashMap;
import java.util.Map;

public class FilterConfigImp implements FilterConfig{
	Map<String,String>configuration=new HashMap<>();
	ServletContext context;
	
	public	FilterConfigImp(Map<String,String>configuration,ServletContext context) {
		this.configuration=configuration;
		this.context=context;
	}
	
	@Override
	public String getFilterName() {
		// TODO Auto-generated method stub
		return configuration.get("filter-name");
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.context;
	}
	
	
	public String getUrl() {
		return configuration.get("url-pattern");
	}

	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		return configuration.get(name);
	}

}
