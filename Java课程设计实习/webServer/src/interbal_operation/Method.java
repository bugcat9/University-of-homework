package interbal_operation;

public enum Method {
	GET("GET"),
	HEAD("HEAD"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE"),
	TRACE("TRACE"),
	CONNECT("CONNECT"),
	UNRECOGNIZED(null);

	final String method;

	Method(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return method;
	}
}
