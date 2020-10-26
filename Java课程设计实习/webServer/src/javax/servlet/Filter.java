package javax.servlet;

import java.io.IOException;

public interface Filter {

	
	 public void init(FilterConfig filterConfig) ;
	 
	 public void doFilter(ServletRequest request, ServletResponse response,
	            FilterChain chain) throws IOException;
	 
	    public void destroy();
}
