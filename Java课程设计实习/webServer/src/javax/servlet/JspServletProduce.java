package javax.servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.omg.CORBA.portable.InputStream;



public class JspServletProduce{
	
	ServletContext Context;
	public JspServletProduce(ServletContext Context) {
		this.Context=Context;
	}
	
	
	StringBuilder ReadJsp(File f) throws IOException {
		BufferedReader reader=new BufferedReader(new  InputStreamReader (new FileInputStream(f),"UTF-8"));
		StringBuilder str=new StringBuilder();
		String temp=null;
		while((temp=reader.readLine())!=null) {
			str.append(temp);
			System.out.println(temp);
		}
	  
		 reader.close();
		 return str;
	}
	
	
	  public String writeServlet(File f) throws IOException {
		  try {
		  StringBuilder str=	ReadJsp(f);

		 String s=this.Context.getContextPath()+"/"+f.getName().replace(".jsp", "")+".java";
		 String  Classname=f.getName().replace(".jsp", "");
		  StringBuffer sb=new StringBuffer();
			BufferedWriter out = new BufferedWriter(new FileWriter(s));
			  sb.append("import java.io.*; \r\n" + 
						"import java.text.*; \r\n" + 
						"import java.util.*;\r\n" + 
						"\r\n" + 
						"import javax.servlet.http.HttpServlet;\r\n" + 
						"import javax.servlet.http.HttpServletRequest;\r\n" + 
						"import javax.servlet.http.HttpServletResponse; \r\n" );
			     sb.append("public class	" +Classname+" extends HttpServlet { \r\n"
						+ "\tpublic void doGet(HttpServletRequest request, HttpServletResponse response)\r\n" + 
						"			  throws IOException\r\n" + 
						"	  {\r\n" + 
						" \t				response.setContentType(\"text/html\");\r\n "+
						"\t				PrintWriter out = response.getWriter(); \r\n"
						);
			     int start=0;
			     int end=0;
			     int i=0;
			     while(str.indexOf("<%")>-1) {
			    	 start=str.indexOf("<%");
			    	 end=str.indexOf("%>");
			    	 sb.append("\t				out.println(\""+str.substring(i,start).replace("\"", "\\\"")+"\");\r\n");
			    	 
			      	 if(str.charAt(start+2)=='=')
			      		 sb.append("\t				out.println("+str.substring(start+3,end)+");\r\n");	 
				    else 
				    	sb.append("\t			"+	str.substring(start+2,end)+"\r\n");
 	 
			    	 str.replace(start, end+2, "");
			    	 i=start;
			     }
			     
			     sb.append("\t				out.println(\""+str.substring(i).replace("\"", "\\\"")+"\");\r\n");
			     sb.append(
							"\t				out.close();\r\n"+
							"	  }\r\n"+
							" \tpublic void doPost(HttpServletRequest request, HttpServletResponse response ) \r\n" + 
							"\t					throws  IOException { \r\n" + 
							"\t					doGet( request,  response);\r\n"+
							"\t	}\r\n"
							+ "}\r\n"
							);
			out.write(sb.toString());
			out.close();
			System.out.println("文件创建成功！");
			return s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	  }
	
public 	static void main(String []args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
	
	try {
		String s="src/Test3.java";
        BufferedWriter out = new BufferedWriter(new FileWriter(s));
        out.write("public class	Test3{\r\n");
        out.write("public 	static void a(String x) {\r\n");
        out.write(" System.out.println(\"成功！\");\r\n");
        out.write(" }\r\n");
        out.write("}\r\n");
        out.close();
        System.out.println("文件创建成功！");
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "-encoding", "UTF-8", "-classpath",s.toString(),s );
      //  E:\Filesave\javacode\webServer\src
        URL[] urls=new URL[] {new URL("file:/"+"E:/Filesave/javacode/webserver/src/")};
        URLClassLoader loader=new URLClassLoader(urls);
        Class<?> c=loader.loadClass("Test3");
        Method []t=c.getMethods();
        System.out.println(t.length);
        for(int i=0;i<t.length;i++)
        {
        	 System.out.println(t[0].getName());
             System.out.println(t[0].getParameterCount());
        }
      
        Method m = c.getDeclaredMethod("a",String.class);
    	//通过Object把数组转化为参数
        String x=null;
    	m.invoke(null,x);

    } catch (IOException e) {
    	
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
}
