package javax.servlet.http;

public interface HttpSession {

	public Object getAttribute(String name);
	
	public void setAttribute(String name, Object value);
}
