package br.com.caelum.vraptor.errormail.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class ErrorMail {
	
	private final String stackTrace;
	private final String referer;
	private final String requestParameters;
	private final Object currentUser;
	private final String subject;
	private final String to;
	private final String from;
	private final String fromName;

	public ErrorMail(String subject, String stackTrace, String referer, String requestParameters, Object currentUser, String to, String from, String fromName) {
		this.subject = subject;
		this.stackTrace = stackTrace;
		this.referer = referer;
		this.requestParameters = requestParameters;
		this.currentUser = currentUser;
		this.to = to;
		this.from = from;
		this.fromName = fromName;
	}

	public String getMsg() {
		String params = "";
		if (hasParameters()) {
			params = "Parameters: " + requestParameters + "\n";
		}
		return "An error occurred and we trapped him: \n\n" + "URL: " + referer + "\n" + params + "User-id : "
				+ (currentUser != null ? currentUser : "UNLOGGED") + "\nException: \n" + stackTrace;
	}
	
	public SimpleEmail toSimpleMail() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.addTo(to);
		email.setSubject(subject);
		email.setMsg(getMsg());
		email.setFrom(from, fromName);
		return email;
	}

	private boolean hasParameters() {
		return requestParameters != null && !requestParameters.isEmpty();
	}

	public String getStackTrace() {
		return stackTrace;
	}
}
