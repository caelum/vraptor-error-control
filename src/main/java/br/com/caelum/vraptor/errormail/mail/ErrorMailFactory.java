package br.com.caelum.vraptor.errormail.mail;

import static br.com.caelum.vraptor.errormail.util.StackToString.convertStackToString;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;

import br.com.caelum.vraptor.environment.Environment;

public class ErrorMailFactory {

	private final HttpServletRequest req;
	private final Environment env;
	private static final String CURRENT_USER = "currentUser";
	protected static final String SIMPLE_MAIL_FROM_NAME = "vraptor.simplemail.main.from.name";
	protected static final String SIMPLE_MAIL_FROM = "vraptor.simplemail.main.from";
	protected static final String REQUEST_PARAMETERS = "javax.servlet.forward.query_string";
	protected static final String REQUEST_URI = "javax.servlet.forward.request_uri";
	protected static final String EXCEPTION = "javax.servlet.error.exception";
	protected static final String TARGET_MAILING_LIST = "vraptor.simplemail.main.error-mailing-list";

	public ErrorMailFactory(HttpServletRequest req, Environment env) {
		this.req = req;
		this.env = env;
	}
	
	public ErrorMail build() throws EmailException{
		Throwable t = (Throwable) req.getAttribute(EXCEPTION);
		String referer = (String) req.getAttribute(REQUEST_URI);
		Object user = (String) req.getAttribute(CURRENT_USER);
		String queryString = "";
		if(req.getMethod().equals("GET")){
			queryString = (String) req.getAttribute(REQUEST_PARAMETERS);
		}
		if(!env.has(TARGET_MAILING_LIST)) {
			throw new EmailException(noMailingListMessage());
		}
		String mailingList = env.get(TARGET_MAILING_LIST);
		String from = env.get(SIMPLE_MAIL_FROM);
		String fromName = env.get(SIMPLE_MAIL_FROM_NAME);
		return new ErrorMail("production error",  convertStackToString(t), referer, queryString, user, mailingList, from, fromName);
	}

	private String noMailingListMessage() {
		return "No target mailing list for error messages was set in " + env.getName() + ". THIS IS HARDCORE, nobody will know about this error.";
	}
}
