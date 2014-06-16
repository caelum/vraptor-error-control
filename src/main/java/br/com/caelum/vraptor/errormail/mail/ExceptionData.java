package br.com.caelum.vraptor.errormail.mail;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class ExceptionData {
	
	public static final String EXCEPTION = "javax.servlet.error.exception";
    public static final String REQUEST_URI = "javax.servlet.forward.request_uri";
	
	private Throwable throwable;
	private String uri;
	private Object user;
	private String queryString;
	public static final String CURRENT_USER = "currentUser";
	private String headers;

	public ExceptionData(Throwable throwable, String uri, Object user, 
			String queryString, String headers) {
		this.throwable = throwable;
		this.uri = uri;
		this.user = user;
		this.queryString = queryString;
		this.headers = headers;
	}
	
	public static ExceptionData fromRequest(HttpServletRequest req) {
		Throwable throwable = (Throwable) req.getAttribute(EXCEPTION);
		String uri = (String) req.getAttribute(REQUEST_URI);
		Object user = req.getAttribute(CURRENT_USER);
		String queryString = "";
		if ("GET".equals(req.getMethod())) {
			queryString = req.getQueryString();
		}
		return new ExceptionData(throwable, uri, user, queryString, buildHeadersMap(req));
	}

	private static String buildHeadersMap(HttpServletRequest req) {
		Enumeration<String> headerNames = req.getHeaderNames();
		StringBuilder headers = new StringBuilder("");
		
		while (headerNames != null && headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			headers.append("    " + name + ": ");
			headers.append(req.getHeader(name) + "\n");
		}
		return headers.toString();

	}

	public Throwable getException() {
		return throwable;
	}
	
	public String getUri() {
		return uri;
	}
	
	public Object getUser() {
		return user;
	}

	public String getQueryString() {
		return queryString;
	}
	
	public String getHeaders() {
		return headers;
	}
}
