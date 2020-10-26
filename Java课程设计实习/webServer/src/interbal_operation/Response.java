package interbal_operation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.servlet.ServletContext;


//监听方的Response
public class Response {
	OutputStream output;
	ServletContext Context;
public Response(OutputStream output,ServletContext Context) {
	this.output=new DataOutputStream(output);
	this.Context=Context;
}

public	void sendFile(String path) throws IOException {
	DataOutputStream out=new DataOutputStream(output);
	File f=Context.getResource(path);
	System.out.println(f.getPath());
	int i=path.lastIndexOf(".");
	ContentType type=ContentType.valueOf(path.substring(i+1).toUpperCase()) ;
	out.writeBytes("HTTP-1.0 200 ok\r\n");//后面有待填坑
	DataInputStream din=new DataInputStream(new FileInputStream(f));
	int len=(int ) f.length();
	byte[] buf=new byte[len];
	din.readFully(buf);
	out.writeBytes("Content-Length:"+len+"\r\n");
	out.writeBytes(type.toString()+" \r\n\r\n");
	out.write(buf);
	out.flush();
	out.close();
}

public void sendDir(String path) throws IOException {
	DataOutputStream out=new DataOutputStream(output);
	Set<String> set=Context.getResourcePaths(path);
	out.writeBytes("HTTP-1.0 200 ok\r\n");//后面有待填坑
	StringBuilder str=new StringBuilder();
	str.append("<!DOCTYPE html>");
	str.append("<html> ");
	str.append("<head>");
	str.append("<meta charset=\"utf-8\">");
	str.append("<title>content</title> ");
	str.append("</head>");
	str.append("<body>");
	for(String s:set)
	{
		//<a href="url">链接文本</a>
		System.out.println(s);
		//填坑
		str.append("<a href=\"http://localhost/"+s.substring(1)+"\">"+s.substring(1)+"</a><br/>");
	}
	str.append("</body>");
	str.append("</html>");
	out.writeBytes("Content-Length:"+str.length()+"\r\n");
	out.writeBytes("Content-Type:text/html \r\n\r\n");
	out.writeBytes(str.toString());
	out.flush();
	out.close();
}


public OutputStream getOutputStream() {
	return this.output;
}
}
