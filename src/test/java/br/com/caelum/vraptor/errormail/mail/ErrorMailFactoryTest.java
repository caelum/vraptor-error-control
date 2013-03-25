package br.com.caelum.vraptor.errormail.mail;

import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.CURRENT_USER;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.DEFAULT_SUBJECT;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.ERROR_DATE_PATTERN;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.EXCEPTION;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.REQUEST_PARAMETERS;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.REQUEST_URI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.environment.DefaultEnvironment;

public class ErrorMailFactoryTest {
	HttpServletRequest request;
	
	@Before
	public void set_up(){
		request = mock(HttpServletRequest.class);
		when(request.getMethod()).thenReturn("GET");
		when(request.getAttribute(EXCEPTION)).thenReturn(exception());
		when(request.getAttribute(REQUEST_URI)).thenReturn("/test");
		when(request.getAttribute(REQUEST_PARAMETERS)).thenReturn("test=true");
		when(request.getAttribute(CURRENT_USER)).thenReturn(new FakeUser());
	}

	@Test
	public void should_build_error_mail_using_request_and_environment() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test"));
		ErrorMail errorMail = factory.build();
		assertThat(errorMail.getMsg(), startsWith(expectedGETMsg()));
	}
	
	@Test
	public void should_not_show_parameters_if_request_method_is_not_get() throws IOException, EmailException {
		when(request.getMethod()).thenReturn("POST");
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test"));
		ErrorMail errorMail = factory.build();
		assertThat(errorMail.getMsg(), startsWith(expectedPOSTMsg()));
	}

	@Test(expected = EmailException.class)
	public void should_not_run_without_target_email_list() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test-without-email-list"));
		factory.build();
		
	}

	@Test(expected = NoSuchElementException.class)
	public void should_not_run_without_from() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test-without-from"));
		factory.build();
	}
		
	@Test
	public void should_use_default_subject_if_doesnt_exist_in_environment() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test-without-subject"));
		ErrorMail email = factory.build();
		assertThat(email.toSimpleMail().getSubject(), equalTo(DEFAULT_SUBJECT));
	}
	
	@Test
	public void should_use_provided_subject_if_exists_in_environment() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test-with-subject"));
		ErrorMail email = factory.build();
		assertThat(email.toSimpleMail().getSubject(), equalTo("Error at 'cat' project"));
	}
	
	@Test
	public void should_include_date_at_subject_if_pattern_is_present() throws IOException, EmailException {
		DefaultEnvironment env = new DefaultEnvironment("test-with-datepattern");
		ErrorMailFactory factory = new ErrorMailFactory(request, env);
		ErrorMail email = factory.build();
		String pattern = forPattern(env.get(ERROR_DATE_PATTERN)).print(DateTime.now());
		assertThat(email.toSimpleMail().getSubject(), equalTo(DEFAULT_SUBJECT+" - "+pattern));
	}
	
	private Throwable exception() {
		return new Exception("my fake exception");
	}
	
	private String expectedGETMsg(){
		return expectedMSG(true);
	}
	
	private String expectedPOSTMsg(){
		return expectedMSG(false);
	}
	
	private String expectedMSG(boolean isGet){
		String parameters = "";
		if(isGet){
			parameters = "Parameters: test=true\n";
		}
		return "An error occurred and we trapped him: \n\n" + 
				"URL: /test\n" + 
				parameters + 
				"User-id : Tester\n" + 
				"Exception: \n" + 
				"java.lang.Exception: my fake exception\n"; 
	}
}
