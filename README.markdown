## vraptor-error-control
![Build status](https://secure.travis-ci.org/caelum/vraptor-error-control.png)

A simple vraptor plugin that allows you to real time control error messages sending them by email

# installing

Vraptor-error-control.jar can be downloaded from maven's repository, or configured in any compatible tool:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-error-control</artifactId>
			<version>1.0.0</version>
			<scope>compile</scope>
		</dependency>


# Dependencies

To use Vraptor Error Control you need to use the following plugins in your classpath:

		vraptor-environment  
		vraptor-simplemail  

# Configuring

In your environment.properties(vraptor-environment files), you need to configure the following
vraptor-simplemail' keys:

		vraptor.simplemail.main.server
		vraptor.simplemail.main.port
		vraptor.simplemail.main.tls
		vraptor.simplemail.main.from.name
		vraptor.simplemail.main.from
		vraptor.simplemail.main.username
		vraptor.simplemail.main.password
		vraptor.simplemail.main.error-mailing-list

In your web.xml file, you need to map the error-code 500 to /error500:
		
		<error-page>
			<error-code>500</error-code>	
			<location>/error500</location>
		</error-page>
		
After sending the email, the vraptor-error-control will forward the request to jsp/error/500.jsp
and include the stack trace as an attribute of the request with the key "stackTrace".

# help

Get help from vraptor developers and the community at vraptor mailing list.
