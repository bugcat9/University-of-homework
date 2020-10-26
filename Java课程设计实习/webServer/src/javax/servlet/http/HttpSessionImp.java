package javax.servlet.http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessionImp implements HttpSession{

	Map<String,Object>Attribute=new HashMap<>();
	public HttpSessionImp()
	{
		
	}
	
	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return Attribute.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		Attribute.put(name, value);
	}

}
