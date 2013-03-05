package br.com.caelum.vraptor.errormail;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.errormail.mail.ErrorMail;
import br.com.caelum.vraptor.errormail.mail.ErrorMailFactory;
import br.com.caelum.vraptor.errormail.mail.ErrorMailer;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.MailerFactory;

@WebServlet(urlPatterns="/error500", displayName="error-servlet")
public class ErrorLoggerServlet extends HttpServlet {
	private static final long serialVersionUID = -3360854860883666693L;
	private ErrorMailer mailer;

	@Override
	public void init() throws ServletException {
		try {
			ServletBasedEnvironment env = new ServletBasedEnvironment(this.getServletContext());
			Mailer mailer = new MailerFactory(env).getInstance();
			this.mailer = new ErrorMailer(mailer, env);
		} catch (IOException e) {
			throw new ServletException("Unable to initialize error servlet", e);
		}
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ErrorMail errorMail = new ErrorMailFactory(req).build();
		mailer.register(errorMail);
		req.setAttribute("stackTrace", errorMail.getStackTrace());
		req.getRequestDispatcher("/WEB-INF/jsp/error/500.jsp").forward(req, res);
	}
}