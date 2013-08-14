package br.com.caelum.vraptor.errormail;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.errormail.mail.ErrorMail;
import br.com.caelum.vraptor.errormail.mail.ErrorMailFactory;
import br.com.caelum.vraptor.errormail.mail.ErrorMailer;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.MailerFactory;

@WebServlet(urlPatterns="/error500", displayName="error-servlet")
public class ErrorLoggerServlet extends HttpServlet {
	private static final long serialVersionUID = -3360854860883666693L;
	private static final Logger logger = LoggerFactory.getLogger(ErrorLoggerServlet.class);
	
	@Inject
	private ErrorMailer mailer;
	
	@Inject
	private ServletBasedEnvironment env;

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			ErrorMail errorMail = new ErrorMailFactory(req, env).build();
			mailer.register(errorMail);
			req.setAttribute("stackTrace", errorMail.getStackTrace());
			req.getRequestDispatcher("/WEB-INF/jsp/error/500.jsp").forward(req, res);
		} catch (EmailException e) {
			logger.error(e.getMessage());
		}
	}
	
}