package br.com.caelum.vraptor.errormail.mail;

import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.CURRENT_USER;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.EXCEPTION;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.REQUEST_PARAMETERS;
import static br.com.caelum.vraptor.errormail.mail.ErrorMailFactory.REQUEST_URI;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.hamcrest.Matchers;
import org.junit.Assert;
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
		assertTrue(errorMail.getMsg().startsWith(expectedGETMsg()));
		assertThat(errorMail.getMsg(), startsWith(expectedGETMsg()));
	}
	
	@Test
	public void should_not_show_parameters_if_request_method_is_not_get() throws IOException, EmailException {
		when(request.getMethod()).thenReturn("POST");
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test"));
		ErrorMail errorMail = factory.build();
		System.out.println(errorMail);
		assertTrue(errorMail.getMsg().startsWith(expectedPOSTMsg()));
	}

	@Test(expected = EmailException.class)
	public void should_not_run_with_no_target_email_list() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test-without-email-list"));
		factory.build();
		
	}

	@Test(expected = NoSuchElementException.class)
	public void should_not_run_with_no_from() throws IOException, EmailException {
		ErrorMailFactory factory = new ErrorMailFactory(request, new DefaultEnvironment("test-without-from"));
		factory.build();
		
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
