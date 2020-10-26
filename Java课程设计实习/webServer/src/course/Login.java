package course;

import java.io.IOException;

import javax.servlet.RequestDispatcher;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
		// TODO Auto-generated method stub
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		
		System.out.println("userName:"+userName+"\t password:"+password);
		if("admin".equals(userName) && "admin".equals(password))
				  request.getSession().setAttribute("username","admin");
		else
			 request.getSession().setAttribute("username","Unknown User");
		
		response.sendRedirect("test6.jsp");
			
	//	doGet(request, response);
	}
	

}
