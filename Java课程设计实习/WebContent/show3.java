import java.io.*; 
import java.text.*; 
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
public class	show3 extends HttpServlet { 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			  throws IOException
	  {
 					response.setContentType("text/html");
 					PrintWriter out = response.getWriter(); 
					out.println("<!DOCTYPE html><html><head><title>Testing for Servlet-MVC</title><body> <h1>Recommended Pet - Testing for Web-MVC</h1> <p>You want a ");
					out.println(request.getParameter("legs"));
					out.println("-legged pet weighing ");
					out.println(request.getParameter("weight"));
					out.println("lbs.</p><p> We recommend getting <b>");
					out.println(request.getAttribute("pet"));
					out.println("</b></p></body> </html>");
					out.close();
	  }
 	public void doPost(HttpServletRequest request, HttpServletResponse response ) 
						throws  IOException { 
						doGet( request,  response);
		}
}
