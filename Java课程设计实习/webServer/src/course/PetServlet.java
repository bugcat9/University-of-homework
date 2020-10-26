package course;

//   The Servlet library is needed to compile this code.
//   That is NOT included in the JDK download.

//   For servlets, you need to download J2EE (currently 1.4)
//   from http://java.sun.com/j2ee/1.4/download.html#sdk
//      or from http://jakarta.apache.org

// compile with javac -Djava.ext.dirs=%TOMCAT_HOME%\common\lib PetServlet.java 
// or make sure the servlet-api.jar is in the CLASSPATH

import javax.servlet.*; 
import javax.servlet.http.*; 
import java.io.*; 
import java.text.*; 
import java.util.*; 

public class PetServlet extends HttpServlet { 

    private String recommendedPet(int weight, int legs) { 
        if (legs ==0) return "a goldfish"; 
        if (legs ==4) { 
           if (weight<20) return "a cat"; 
           if (weight<100) return "a dog"; 
        } 
        return "a house plant"; 
    } 

    public void doPost(HttpServletRequest req, 
                       HttpServletResponse resp ) 
        throws  IOException { 

        // get the input field values 
        int petWeight = 0, petLegs = 0; 
        try { 
          petWeight = Integer.parseInt(req.getParameter("weight")); 
          petLegs = Integer.parseInt(req.getParameter("legs")); 
        } catch (NumberFormatException nfe) { 
          petWeight=petLegs=-1; // indicates that we got an invalid number
        } 

        resp.setContentType("text/html"); 

        PrintWriter out = resp.getWriter(); 

        out.println(" <html> <body> <h1>Recommended Pet - Testing for Servlet</h1> <p>"); 
        out.println("You want a " + petLegs + "-legged pet weighing " 
                   + petWeight + "lbs."); 

        String pet = recommendedPet(petWeight, petLegs); 
        out.println("<P> We recommend getting <b>" + pet ); 
        out.println("</b> <hr> </body> </html> "); 

        out.close(); 
    } 
 
} 
