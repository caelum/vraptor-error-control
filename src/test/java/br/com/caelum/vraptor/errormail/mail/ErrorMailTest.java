package br.com.caelum.vraptor.errormail.mail;

import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorMailTest {

	@Test
	public void should_not_show_parameters_if_its_empty() {
		ErrorMail errorMail = new ErrorMail("test", "my stack", "/my/url", "", null);
		assertFalse(errorMail.getMsg().contains("Parameters: "));
	}
	
	@Test
	public void should_contains_parameters() {
		ErrorMail errorMail = new ErrorMail("test", "my stack", "/my/url", "my=param", null);
		assertTrue(errorMail.getMsg().contains("Parameters: "));
	}
	
	@Test
	public void should_get_full_message() {
		String expected = "test\n\nURL: /my/url\nParameters: my=param\nUser-id :" +
				" UNLOGGED\nException: \nmy stack";
		ErrorMail errorMail = new ErrorMail("test", "my stack", "/my/url", "my=param", null);
		assertEquals(expected ,errorMail.getMsg());
	}

}
