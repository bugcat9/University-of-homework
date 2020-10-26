import java.io.*; 
import java.text.*; 
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
public class	show5 extends HttpServlet { 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			  throws IOException
	  {
 					response.setContentType("text/html");
 					PrintWriter out = response.getWriter(); 
					out.println("<!DOCTYPE html><html><head><title>Testing for Filter</title><body> <h1>Testing for Filter</h1> <p>The site have been visited ");
					out.println(course.AccessFilter.nNum);
					out.println(" times.<p></body> </html>");
					out.close();
	  }
 	public void doPost(HttpServletRequest request, HttpServletResponse response ) 
						throws  IOException { 
						doGet( request,  response);
		}
}
