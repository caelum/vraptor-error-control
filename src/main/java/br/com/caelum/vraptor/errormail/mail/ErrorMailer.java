package br.com.caelum.vraptor.errormail.mail;

import javax.inject.Inject;

import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.simplemail.Mailer;

public class ErrorMailer {
	private final Mailer mailer;
	private static Logger logger = LoggerFactory.getLogger(ErrorMailer.class);
	
	@Deprecated
	ErrorMailer() {
		this(null);
	}

	@Inject
	public ErrorMailer(Mailer mailer) {
		this.mailer = mailer;
	}

	public void register(ErrorMail errorMail) {
		logger.error(errorMail.getMsg());
		try {
			SimpleEmail email = errorMail.toSimpleMail();
			mailer.send(email);
		} catch (Exception ex) {
			logger.error("Unable to send error by email. THIS IS HARDCORE, nobody will know about this error.", ex);
		}
	}
}
