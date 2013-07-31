package br.com.caelum.vraptor.errormail.mail;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.apache.commons.mail.EmailException;
import org.junit.Test;

public class ErrorMailTest {
	
	private DefaultErrorMail defaultErrorMail = new DefaultErrorMail("test", "my stack", "/my/url", "my=param", null, 
			"mailinglist@list.com", "vraptor@vraptor", "vraptor", "header-key: value\n");

	@Test
	public void should_not_show_parameters_if_its_empty() {
		DefaultErrorMail errorMail = new DefaultErrorMail("test", "my stack", "/my/url", "", null, "mailinglist@list.com", 
				"vraptor@vraptor", "vraptor", "header-key: value\n");
		assertThat(errorMail.getMsg(), not(containsString("Parameters: ")));
	}
	
	@Test
	public void should_contains_parameters() {
		assertThat(defaultErrorMail.getMsg(), containsString("Parameters: "));
	}
	
	@Test
	public void should_get_full_message() {
		String expected = "An error occurred and we trapped him: \n\nURL: /my/url\nParameters: my=param\nHeaders:\nheader-key: value\nUser-id :" +
				" UNLOGGED\nException: \nmy stack";
		assertThat(defaultErrorMail.getMsg(), equalTo(expected));
	}
	
	@Test
	public void should_convert_to_simple_mail() throws EmailException{
		defaultErrorMail.toSimpleMail();
	}

}
