package br.com.caelum.vraptor.errormail.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.mail.EmailException;
import org.junit.Test;

public class ErrorMailTest {
	
	ErrorMail defaultErrorMail = new ErrorMail("test", "my stack", "/my/url", "my=param", null, "mailinglist@list.com", "vraptor@vraptor", "vraptor");
	

	@Test
	public void should_not_show_parameters_if_its_empty() {
		ErrorMail errorMail = new ErrorMail("test", "my stack", "/my/url", "", null, "mailinglist@list.com", "vraptor@vraptor", "vraptor");
		assertFalse(errorMail.getMsg().contains("Parameters: "));
	}
	
	@Test
	public void should_contains_parameters() {
		assertTrue(defaultErrorMail.getMsg().contains("Parameters: "));
	}
	
	@Test
	public void should_get_full_message() {
		String expected = "An error occurred and we trapped him: \n\nURL: /my/url\nParameters: my=param\nUser-id :" +
				" UNLOGGED\nException: \nmy stack";
		assertEquals(expected ,defaultErrorMail.getMsg());
	}
	
	@Test
	public void should_convert_to_simple_mail() throws EmailException{
		defaultErrorMail.toSimpleMail();
	}

}
