package webservlet;

import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class loginServlet extends HttpServlet{
	
	public loginServlet(){
		super();
		System.out.println("我被反射了1");
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("loginServlet is used");
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String s= "webservlet.loginServlet";
	   Class a= Class.forName(s); 
	   HttpServlet  hs=(loginServlet)a.newInstance();
	}



	@Override
	public String getServletName() {
		// TODO Auto-generated method stub
		return null;
	}

}
