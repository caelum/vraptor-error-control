package br.com.caelum.vraptor.errormail.mail;

import static br.com.caelum.vraptor.errormail.util.StackToString.convertStackToString;
import static org.joda.time.format.DateTimeFormat.forPattern;

import java.util.NoSuchElementException;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;

import br.com.caelum.vraptor.environment.Environment;

public class ErrorMailFactory {

	private final Environment env;
	private final ExceptionData req;
	public static final String SIMPLE_MAIL_FROM_NAME = "vraptor.simplemail.main.from.name";
	public static final String SIMPLE_MAIL_FROM = "vraptor.simplemail.main.from";
	public static final String REQUEST_PARAMETERS = "javax.servlet.forward.query_string";
	public static final String TARGET_MAILING_LIST = "vraptor.simplemail.main.error-mailing-list";
	public static final String ERROR_DATE_PATTERN = "vraptor.errorcontrol.date.joda.pattern";
	public static final String DEFAULT_SUBJECT = "production error";
	private static final String ERROR_MAIL_SUBJECT = "vraptor.errorcontrol.error.subject";
	private static final String USE_MESSAGE_AS_SUBJECT = "vraptor.errorcontrol.exception.subject";

	public ErrorMailFactory(ExceptionData req, Environment env) {
		this.req = req;
		this.env = env;
	}

	public ErrorMail build() throws EmailException {
		Throwable t = req.getException();
		String referer = req.getUri();
		Object user = req.getUser();
		
		String queryString = req.getQueryString();
		if (!env.has(TARGET_MAILING_LIST)) {
			throw new EmailException(noMailingListMessage());
		}
		String mailingList = env.get(TARGET_MAILING_LIST);
		String from = env.get(SIMPLE_MAIL_FROM);
		String fromName = env.get(SIMPLE_MAIL_FROM_NAME);
		String headers = req.getHeaders();
		String subject = getSubject();

		return new DefaultErrorMail(subject, convertStackToString(t), referer,
				queryString, user, mailingList, from, fromName, headers);
	}


	private String getSubject() {
		StringBuilder subject = new StringBuilder();
		subject.append(getProperty(ERROR_MAIL_SUBJECT, DEFAULT_SUBJECT));
		try{
			String pattern = env.get(ERROR_DATE_PATTERN);
			subject.append(" - "+forPattern(pattern).print(DateTime.now()));
		}catch (NoSuchElementException e) {
		}
		if(Boolean.valueOf(getProperty(USE_MESSAGE_AS_SUBJECT, "false"))){
			subject.append(" - " + req.getException().getMessage());
		}
		return subject.toString();
	}
	
	private String getProperty(String firstProperty, String defaultProperty) {
		try {
			return env.get(firstProperty);
		} catch (NoSuchElementException e) {
			return defaultProperty;
		}
	}

	private String noMailingListMessage() {
		return "No target mailing list for error messages was set in " + env.getName() + ". THIS IS HARDCORE, nobody will know about this error.";
	}

}
