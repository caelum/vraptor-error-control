package br.com.caelum.vraptor.errormail.mail;

import static br.com.caelum.vraptor.errormail.util.StackToString.convertStackToString;

import javax.servlet.http.HttpServletRequest;

public class ErrorMailFactory {

	private final HttpServletRequest req;

	public ErrorMailFactory(HttpServletRequest req) {
		this.req = req;
	}
	
	public ErrorMail build(){
		Throwable t = (Throwable) req.getAttribute("javax.servlet.error.exception");
		String referer = (String) req.getAttribute("javax.servlet.forward.request_uri");
		String queryString = "";
		if(req.getMethod() == "GET"){
			queryString = (String) req.getAttribute("javax.servlet.forward.query_string");
		}
		Object user = (String) req.getAttribute("currentUser");
		return new ErrorMail("An error occurred and we trapped him: ",  convertStackToString(t), referer, queryString, user);
	}

}
