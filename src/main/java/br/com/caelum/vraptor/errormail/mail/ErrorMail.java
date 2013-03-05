package br.com.caelum.vraptor.errormail.mail;


public class ErrorMail {
	
	private final String title;
	private final String stackTrace;
	private final String referer;
	private final String requestParameters;
	private final Object user;

	public ErrorMail(String title, String stackTrace, String referer, String requestParameters, Object user) {
		this.title = title;
		this.stackTrace = stackTrace;
		this.referer = referer;
		this.requestParameters = requestParameters;
		this.user = user;
	}

	public String getMsg() {
		String params = "";
		if (hasParameters()) {
			params = "Parameters: " + requestParameters + "\n";
		}
		return title+"\n\n" + "URL: " + referer + "\n" + params + "User-id : "
				+ (user != null ? user : "UNLOGGED") + "\nException: \n" + stackTrace;
	}

	private boolean hasParameters() {
		return requestParameters != null && !requestParameters.isEmpty();
	}

	public String getStackTrace() {
		return stackTrace;
	}
}
