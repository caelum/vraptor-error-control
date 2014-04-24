package br.com.caelum.vraptor.errormail.mail;

import static br.com.caelum.vraptor.environment.EnvironmentType.DEVELOPMENT;
import static br.com.caelum.vraptor.environment.EnvironmentType.PRODUCTION;
import static br.com.caelum.vraptor.environment.EnvironmentType.TEST;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.DEFAULT_SUBJECT;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.ERROR_DATE_PATTERN;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.environment.DefaultEnvironment;

public class ErrorMailFactoryTest {
	private ExceptionData request;
	
	@Before
	public void set_up(){
		request = new ExceptionData(exception(), "/test", new FakeUser(), "test=true", "");
	}

	@Test
	public void should_build_error_mail_using_request_and_environment() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment(TEST));
		ErrorMail errorMail = factory.build();
		assertThat(errorMail.getMsg(), startsWith(expectedMSG()));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void should_not_run_without_from() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment(PRODUCTION));
		factory.build();
	}
	
	@Test
	public void should_work_with_fake_request() throws Exception {
		DefaultEnvironment env = new DefaultEnvironment(TEST);
		
		ExceptionData req = new ExceptionData(new Exception("test"), null, null, null, null);
		
		ErrorMail mail = new ErrorMailFactory(req, env).build();
		assertNotNull(mail);
	}
		
	@Test
	public void should_use_default_subject_if_doesnt_exist_in_environment() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment(DEVELOPMENT));
		ErrorMail email = factory.build();
		assertThat(email.toSimpleMail().getSubject(), equalTo(DEFAULT_SUBJECT));
	}
	
	@Test
	public void should_use_provided_subject_if_exists_in_environment() throws IOException, EmailException {
		DefaultEnvironment env = new DefaultEnvironment(TEST);
		ErrorMailFactory factory = new ErrorMailFactory(request, env);
		ErrorMail email = factory.build();
		String pattern = forPattern(env.get(ERROR_DATE_PATTERN)).print(DateTime.now());
		assertThat(email.toSimpleMail().getSubject(), equalTo("Error at 'cat' project" + " - " + pattern));
	}
	
	private Throwable exception() {
		return new Exception("my fake exception");
	}
	
	
	private String expectedMSG(){
		return "An error occurred and we trapped him: \n\n" + 
				"URL: /test\n" + 
				"Parameters: test=true\n" + 
				"Headers:\n" + 
				"User-id : Tester\n" + 
				"Exception: \n" + 
				"java.lang.Exception: my fake exception\n"; 
	}
}
