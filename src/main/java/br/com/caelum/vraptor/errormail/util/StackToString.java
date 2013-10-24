package br.com.caelum.vraptor.errormail.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackToString {
	public static String convertStackToString(Throwable t){
		if(t == null) return "";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		return sw.getBuffer().toString();
	}
}
