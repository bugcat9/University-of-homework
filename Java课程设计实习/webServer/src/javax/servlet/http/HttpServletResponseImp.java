package javax.servlet.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLEngineResult.Status;

public class HttpServletResponseImp implements HttpServletResponse{
	
	Map<String,String>Info=new HashMap<>();
	OutputStream output;
	PrintWriter  writer;
	public  HttpServletResponseImp(OutputStream output){
		this.output=output;
		writer=new PrintWriter(output);
		
	}
	
	
	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		//writer.write("HTTP-1.0 200 ok\r\n");
		return writer;
	}

	@Override
	public void setContentLength(int len) {
		// TODO Auto-generated method stub
		writer.write("Content-Length:"+len+"\r\n");
		Info.put("Content-Length:", len+"");
	}

	@Override
	public void setContentType(String type) {
		// TODO Auto-generated method stub
		writer.write("HTTP-1.0 200 ok\r\n");
		writer.write("Content-Type:"+type+ "\r\n\r\n");
		Info.put("Content-Type:", type);
	}

	@Override
	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		Status s=Status.valueOf("_"+sc);
		writer.write("HTTP-1.0 "+s+"\\r\\n:");
		Info.put("Status:", sc+"");
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return Integer.valueOf( Info.get("Status"));
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		if(Info.containsKey(name))
			return Info.get(name);
		return null;
	}


	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		writer.write("HTTP-1.0 302 ok\r\n");
		writer.write("Location:/"+location+"\r\n\r\n");
		writer.close();
		
	}

}
