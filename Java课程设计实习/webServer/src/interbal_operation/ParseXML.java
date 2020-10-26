package interbal_operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


//解析XML
public class ParseXML {
	
	/***
	 * 
	 * @param url
	 * @return
	 * @throws DocumentException
	 */
	public static  void ReadXML(String  url) throws DocumentException {
		Map<String,String> map=new HashMap<>();
		SAXReader reader = new SAXReader();
	    Document document = reader.read("WebContent/WEB-INF/web.xml");	//读url
	    Element root = document.getRootElement();
	    
	    Iterator<Element> it = root.elementIterator();
	    String classname="";
	    String url_pattern;
	    while(it.hasNext()) 
	    {
	    	Element ele = it.next();
	    	Iterator<Element> it1=ele.elementIterator();
	    	System.out.println(ele.getName());
	    	while(it1.hasNext()) {
	    		Element e = it1.next();
	    		System.out.println("		"+e.getName()+":"+e.getText());
	    	}
	    	
	    	if(ele.getName().equals("servlet")) {
	    		 Element	servletclass=ele.element("servlet-class");
	    		 classname=servletclass.getText();
	    	}
	    	
	    	if(ele.getName().equals("servlet-mapping")) {
	    		   Element urlpattern=ele.element("url-pattern");
	    		   url_pattern=urlpattern.getText();
	    		   map.put(url_pattern,classname);
	    	}
	    }
	  	
	}
	
	
	
	public static void ReadJsp(String url) throws IOException {
		File f=new File(url);
		BufferedReader reader=new BufferedReader(new  InputStreamReader (new FileInputStream(f),"UTF-8"));
		BufferedWriter out = new BufferedWriter(new FileWriter("11111111111.java"));
		
		StringBuilder str=new StringBuilder();
		StringBuilder sb=new StringBuilder();
		String temp=null;
		String pattern="<%=.*%>";
		// 创建 Pattern 对象
	      Pattern r = Pattern.compile(pattern);
	     // 现在创建 matcher 对象
		while((temp=reader.readLine())!=null) {
			str.append(temp);
			System.out.println(temp);
		}
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println(str.toString());
		System.out.println("---------------------------------------------------------------------------------");
		sb.append("import java.io.*; \r\n" + 
					"import java.text.*; \r\n" + 
					"import java.util.*;\r\n" + 
					"\r\n" + 
					"import ServletPackage.HttpServlet;\r\n" + 
					"import ServletPackage.HttpServletRequest;\r\n" + 
					"import ServletPackage.HttpServletResponse; \r\n" );
	     sb.append("public class	" +"11111111111"+" extends HttpServlet { \r\n"
					+ "\tpublic void doGet(HttpServletRequest request, HttpServletResponse response)\r\n" + 
					"			  throws IOException\r\n" + 
					"	  {\r\n" + 
					" \t				response.setContentType(\"text/html\");\r\n "+
					"\t				PrintWriter out = response.getWriter(); \r\n");
		// 现在创建 matcher 对象
	     Matcher m = r.matcher(str.toString());
	     int start=0;
	     int end=0;
	     int i=0;
	     while(str.indexOf("<%")>-1) {
	    	 start=str.indexOf("<%");
	    	 end=str.indexOf("%>");
	    	 System.out.println(str.substring(i,start));
	    	 sb.append(
	    				"\t				out.println(\""+str.substring(i,start).replace("\"", "\\\"")+"\");\r\n");
	    	 if(str.charAt(start+2)=='=')
	    	 {
	    		 System.out.println(str.substring(start+3,end));
	    		 sb.append(
		    				"\t				out.println("+str.substring(start+3,end)+");\r\n"
		    				);
	    	 }
	    	 else {
	    		 System.out.println(str.substring(start+2,end));
	    		 sb.append(
		    			"\t			"+	str.substring(start+2,end)+"\r\n"
		    				);
	    	 }
	    		 
//	    	 System.out.println("start(): "+m.start());
//	    	 System.out.println("end(): "+m.end());
//	    	 System.out.println(str.substring(m.start()+3, m.end()-2));
//	    	 System.out.println(str.substring(0,m.start()));
//	    	 System.out.println(str.substring(m.end()));
	    	
	    	 	str.replace(start, end+2, "");
	    	 	i=start;
	     }
	     System.out.println(str.substring(i).replace("\"", "\\\""));
	     sb.append("\t				out.println(\""+str.substring(i)+"\");\r\n");
	     reader.close();
	     	sb.append(
				"\t				out.close();\r\n"+
				"	  }\r\n"+
				" \tpublic void doPost(HttpServletRequest request, HttpServletResponse response ) \r\n" + 
				"\t					throws  IOException { \r\n" + 
				"\t					doGet(HttpServletRequest request, HttpServletResponse response);\r\n"+
				"\t	}\r\n"
				+ "}\r\n"
				);
	     System.out.println("---------------------------------------------------------------------------------");
	     System.out.println(sb.toString());
	     out.write(sb.toString());
	     out.close();
	     
	}
	
	
	public static void main(String [] args) throws DocumentException, IOException {
		ReadXML("WebContent/test4.jsp");
	}
	
    
}
