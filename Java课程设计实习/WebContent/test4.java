import java.io.*; 
import java.text.*; 
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
public class	test4 extends HttpServlet { 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			  throws IOException
	  {
 					response.setContentType("text/html");
 					PrintWriter out = response.getWriter(); 
					out.println("<html> 此工作属于提高要求<body bgcolor=\"white\"> <h1>The Echo JSP - Testing for Jsp tasks</h1> ");
				   java.util.Enumeration eh = request.getHeaderNames();      while (eh.hasMoreElements()) {          String h = (String) eh.nextElement();          out.print("<br> header: " + h );          out.println(" value: " + request.getHeader(h));      } 
					out.println(" </body> </html> ");
					out.close();
	  }
 	public void doPost(HttpServletRequest request, HttpServletResponse response ) 
						throws  IOException { 
						doGet( request,  response);
		}
}
