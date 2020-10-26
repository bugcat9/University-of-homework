import java.io.*; 
import java.text.*; 
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
public class	test6 extends HttpServlet { 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			  throws IOException
	  {
 					response.setContentType("text/html");
 					PrintWriter out = response.getWriter(); 
					out.println("<html> <body><b>Login to System</B>Current user is:");
					out.println(request.getSession().getAttribute("username"));
					out.println(" </br> </hr><form  action=\"Login\" method=\"post\">    <h4> User Name: </h4>        <input type=\"text\"  name=\"username\"  size=\"10\">   <h4> Password: </h4>   <input type=\"text\"  name=\"password\"  size=\"10\">        <p>    <input type=\"submit\" value=\"Login\" >    </p></body></form>");
					out.close();
	  }
 	public void doPost(HttpServletRequest request, HttpServletResponse response ) 
						throws  IOException { 
						doGet( request,  response);
		}
}
