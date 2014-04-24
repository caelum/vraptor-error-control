package br.com.caelum.vraptor.errormail.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.junit.Test;

public class ExceptionDataTest {

	@Test
	public void should_not_show_parameters_if_request_method_is_not_get()
			throws IOException, EmailException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getMethod()).thenReturn("POST");
		
		ExceptionData data = ExceptionData.fromRequest(req);
		
		assertEquals("", data.getQueryString());
	}

}
