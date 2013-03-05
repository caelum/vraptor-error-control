package br.com.caelum.vraptor.errormail.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.simplemail.Mailer;

@Component
public class ErrorMailer {
	private static final String TARGET_MAILING_LIST = "vraptor.simplemail.main.error-mailing-list";
	private final Mailer mailer;
	private final Environment env;
	private static Logger logger = LoggerFactory.getLogger(ErrorMailer.class);

	public ErrorMailer(Mailer mailer, Environment env) {
		this.mailer = mailer;
		this.env = env;
	}

	private String noMailingListMessage() {
		return "No target mailing list for error messages was set in " + env.getName() + ". THIS IS HARDCORE, nobody will know about this error.";
	}

	public void register(ErrorMail errorMail) {
		logger.error(errorMail.getMsg());
		if(!env.has(TARGET_MAILING_LIST)) {
			logger.error(noMailingListMessage());
			return;
		}
		try {
			SimpleEmail email = toSimpleMail(errorMail);
			mailer.send(email);
		} catch (Exception ex) {
			logger.error("Unable to send error by email. THIS IS HARDCORE, nobody will know about this error.", ex);
		}
	}

	public SimpleEmail toSimpleMail(ErrorMail errorMail) throws EmailException {
		SimpleEmail email = new SimpleEmail();
		String mailingList = env.get(TARGET_MAILING_LIST);
		email.addTo(mailingList);
		email.setSubject("production error");
		email.setMsg(errorMail.getMsg());
		String from = env.get("vraptor.simplemail.main.from");
		String fromName = env.get("vraptor.simplemail.main.from.name");
		email.setFrom(from, fromName);
		return email;
	}
}
