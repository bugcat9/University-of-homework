import java.io.*; 
import java.text.*; 
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
public class	test2 extends HttpServlet { 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			  throws IOException
	  {
 					response.setContentType("text/html");
 					PrintWriter out = response.getWriter(); 
					out.println("<b>Testing for first JSP</b><br><b> current time is:     ");
					out.println(  new java.util.Date() );
					out.println(" </b> ");
					out.close();
	  }
 	public void doPost(HttpServletRequest request, HttpServletResponse response ) 
						throws  IOException { 
						doGet( request,  response);
		}
}
