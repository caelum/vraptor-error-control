package br.com.caelum.vraptor.errormail.util;

import static br.com.caelum.vraptor.errormail.util.StackToString.convertStackToString;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StackToStringTest {

	@Test
	public void should_return_empty_string_if_throwable_is_null() {
		assertTrue(convertStackToString(null).isEmpty());
	}
	
	@Test
	public void should_return_stack_of_throwable() {
		RuntimeException exception = new RuntimeException();
		String stack = convertStackToString(exception);
		assertThat(stack, containsString(exception.getClass().getSimpleName()));
	}
	
	@Test
	public void should_get_root_exception() {
		IllegalArgumentException rootException = new IllegalArgumentException();
		RuntimeException exception = new RuntimeException(rootException);
		String stack = convertStackToString(exception);
		assertThat(stack, containsString(rootException.getClass().getSimpleName()));
	}
	
	@Test
	public void should_get_cause_messages() {
		String rootCauseMessage = "Beagles are dogs, not cows";
		IllegalArgumentException rootException = new IllegalArgumentException(rootCauseMessage);
		String causeMessage = "Dogs are not cows";
		RuntimeException exception = new RuntimeException(causeMessage, rootException);
		String stack = convertStackToString(exception);
		assertThat(stack, containsString(rootCauseMessage));
		assertThat(stack, containsString(causeMessage));
	}

}
