package br.com.caelum.vraptor.errormail.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public interface ErrorMail {

	SimpleEmail toSimpleMail() throws EmailException;
	String getMsg();
	String getStackTrace();

}
