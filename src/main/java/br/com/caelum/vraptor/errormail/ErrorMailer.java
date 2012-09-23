package br.com.caelum.vraptor.errormail;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.simplemail.AsyncMailer;

@Component
public class ErrorMailer {
	private static final String TARGET_MAILING_LIST = "vraptor.simplemail.main.error-mailing-list";
	private final AsyncMailer mailer;
	private final Environment env;

	public ErrorMailer(AsyncMailer mailer, Environment env) {
		this.mailer = mailer;
		this.env = env;
	}

	private static Logger logger = LoggerFactory.getLogger(ErrorMailer.class);

	public void register(String msg, Throwable e) {
		logger.error(msg, e);

		if(!env.has(TARGET_MAILING_LIST)) {
			logger.error(noMailingListMessage());
			return;
		}
		
		try {
			SimpleEmail email = createEmailFor(msg, e);
			mailer.asyncSend(email);
		} catch (Exception ex) {
			logger.error("Unable to send error by email. THIS IS HARDCORE, nobody will know about this error.", ex);
		}
	}

	private SimpleEmail createEmailFor(String msg, Throwable e)
			throws EmailException {
		SimpleEmail email = new SimpleEmail();
		String mailingList = env.get(TARGET_MAILING_LIST);
		email.addTo(mailingList);
		email.setSubject("production error");
		email.setMsg(msg + "\nException: \n" + stackAsString(e));
		String from = env.get("vraptor.simplemail.main.from");
		String fromName = env.get("vraptor.simplemail.main.from.name");
		email.setFrom(from, fromName);
		return email;
	}

	private String noMailingListMessage() {
		return "No target mailing list for error messages was set in " + env.getName() + ". THIS IS HARDCORE, nobody will know about this error.";
	}

	private String stackAsString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		return sw.getBuffer().toString();
	}

	public void register(String msg, HttpServletRequest req, Throwable t) {
		String referer = (String) req
				.getAttribute("javax.servlet.forward.request_uri");
		String queryString = (String) req
				.getAttribute("javax.servlet.forward.query_string");
		String params = "";
		if (req.getMethod().equals("GET")) {
			params = "Parameters: " + queryString + "\n";
		}
		Object user = req.getAttribute("currentUser");
		msg += "\n\n" + "URL: " + referer + "\n" + params + "User-id : "
				+ (user != null ? user : "UNLOGGED");
		register(msg, t);

	}
}
