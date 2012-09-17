package br.com.caelum.vraptor.errormail;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.simplemail.DefaultAsyncMailer;
import br.com.caelum.vraptor.simplemail.aws.AmazonSESMailer;

@WebServlet(urlPatterns="/error500", displayName="error-servlet")
public class ErrorLoggerServlet extends HttpServlet {
	private static final long serialVersionUID = -3360854860883666693L;
	private ErrorMailer mailer;

	@Override
	public void init() throws ServletException {
		try {
			ServletBasedEnvironment env = new ServletBasedEnvironment(this.getServletContext());
			ExecutorService pool = Executors.newCachedThreadPool();
			DefaultAsyncMailer impl = new DefaultAsyncMailer(pool, new AmazonSESMailer(env));
			mailer = new ErrorMailer(impl, env);
		} catch (IOException e) {
			throw new ServletException("Unable to initialize error servlet", e);
		}
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Throwable t = (Throwable) req.getAttribute("javax.servlet.error.exception");
		mailer.register("An error occurred and we trapped him: ", req, t);

		req.getRequestDispatcher("/WEB-INF/jsp/error/500.jsp").forward(req, res);
	}
}