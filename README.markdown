## vraptor-error-control
![Build status](https://secure.travis-ci.org/caelum/vraptor-error-control.png)

A simple vraptor plugin that allows you to real time control error messages sending them by email

# installing

Vraptor-error-control.jar can be downloaded from maven's repository, or configured in any compatible tool:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-error-control</artifactId>
			<version>4.0.2</version>
			<scope>compile</scope>
		</dependency>


# Dependencies

To use VRaptor Error Control you need to put [vraptor-simplemail](https://github.com/caelum/vraptor-simplemail) at your classpath 


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

To customize the email's subject, use the key bellow:

		vraptor.errorcontrol.error.subject

You can display when the email was sent by adding a [joda time pattern](http://joda-time.sourceforge.net/apidocs/org/joda/time/format/DateTimeFormat.html) using the following key:

		vraptor.errorcontrol.date.joda.pattern

In your web.xml file, you need to map the error-code 500 to /error500:

		<error-page>
			<error-code>500</error-code>
			<location>/error500</location>
		</error-page>

After sending the email, the vraptor-error-control will forward the request to jsp/error/500.jsp
and include the stack trace as an attribute of the request with the key "stackTrace".

# help

Get help from vraptor developers and the community at vraptor mailing list.
