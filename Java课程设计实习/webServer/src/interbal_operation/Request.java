package interbal_operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

//监听方的Request
public class Request {
	
	BufferedReader in=null;
	Method HeadMethod=null;
	Map<String,String>Info=new  HashMap<>();
	StringBuilder reqData=new StringBuilder();
	ServletContext Context;
//需要一个输入流
 public Request(InputStream input,ServletContext Context) {
				in=new BufferedReader(
						new InputStreamReader(input));
				this.Context=Context;
}
 
 public Method getMethod() {
	 return this.HeadMethod;
 }
			
public BufferedReader getBufferedReader() {
	return this.in;
}
 
 
 
public Map<String,String> getInfo(){
	return Info;
}
 

public StringBuilder getData() {
	return this.reqData;
}
 
//解析request,获得想要的数据
 public String getRequest() throws IOException {
		String s = null;
		String res = null;
		while ((s = in.readLine()) != null) {
			System.out.println("got:" + s);
			
			if (s.indexOf("GET") > -1 || s.indexOf("POST") > -1)// 目前只处理GET、POST两种请求
			{
				this.Context.log("got:" + s+"\r\n");
				if (s.indexOf("GET") > -1) {
					
					HeadMethod = Method.GET;
					s = s.substring(4);
					int i = s.indexOf(" ");
				
					res = s.substring(0, i);
					Info.put("GET", s);
				} else {
					this.Context.log("got:" + s+"\r\n");
					HeadMethod = Method.POST;
					s = s.substring(5);
					int i = s.indexOf(" ");
					res = s.substring(0, i);
					Info.put("POST", s);
				}
			} else if (s.hashCode() != 0) {

				String[] a = s.split(":");
				Info.put(a[0], a[1].substring(1));
			} else {

				if (Info.containsKey("Content-Length")) {
					int length = Integer.valueOf(Info.get("Content-Length"));
					char[] cbuf = new char[length];
					System.out.println(in.read(cbuf));
					reqData.append(cbuf);
					System.out.println(reqData);
				}
				break;
			}
		}
		return res;
	}

 
 
}
