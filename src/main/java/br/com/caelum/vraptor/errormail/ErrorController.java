package br.com.caelum.vraptor.errormail;

import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Path;

@Controller
public class ErrorController {

	@Path("/egg/hiuvfh84m8")
	public void error() {
		throw new RuntimeException("Unable to do anything");
	}
}