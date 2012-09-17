package br.com.caelum.vraptor.errormail;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
public class ErrorController {

	@Path("/egg/hiuvfh84m8")
	public void error() {
		throw new RuntimeException("Unable to do anything");
	}
}