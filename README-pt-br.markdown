## vraptor-error-control

Um plugin simples para o VRaptor que permite o controle em tempo real de mensagens de erro enviando-as por email.

# installing

O arquivo vraptor-error-control.jar pode ser baixado do repositório do Maven ou configurado em qualquer ferramenta compatível:

		<dependency>
			<groupId>br.com.caelum.vraptor</groupId>
			<artifactId>vraptor-error-control</artifactId>
			<version>1.0.0</version>
			<scope>compile</scope>
		</dependency>
		
		
# Dependências
Para usar o Vraptor Error Control é necessário ter os seguintes plugins no classpath:

		vraptor-environment  
		vraptor-simplemail  

# Configurando
No seu arquivo environment.properties(do vraptor-environment), é necessário configurar as seguintes chaves
do vraptor-simplemail:

		vraptor.simplemail.main.server
		vraptor.simplemail.main.port
		vraptor.simplemail.main.tls
		vraptor.simplemail.main.from.name
		vraptor.simplemail.main.from
		vraptor.simplemail.main.username
		vraptor.simplemail.main.password
		vraptor.simplemail.main.error-mailing-list

Em seu arquivo web.xml, é necessário mapear o error-code 500 para /error500:
		
		<error-page>
			<error-code>500</error-code>	
			<location>/error500</location>
		</error-page>
		
(Depois de enviar o email, o vraptor-error-control irá redirecionar a requisição para jsp/error/500.jsp)

# help

Para maiores informações, consulte a lista de e-mails da comunidade VRaptor.
