How to use Email Application:
- Add the dependency in pom.xml
- Scan the beans defined in the email project by adding package [org.openframework.commons.email] or Class (EmailApplication.class) to scan.
	- e.g. Add EmailApplication.class in @SpringBootApplication OfdsApplication
- To load the Spring's JavaMailSender.class, configure spring's mail properties in application.yml
	- Create a copy of [EmailApplication/resources/application-email.yml] file and name it application-<profile->email.yml
	- Save above application-<env->email.yml file in your spring boot app's classpath
	- Override the properties in above file as applicable.
	- Add the <profile>-email into application's as shown below:
		spring:
		  profiles:
    		include: dev-jdbc, dev-email
	- Refer: https://stackoverflow.com/questions/35663679/spring-boot-inherit-application-properties-from-dependency/43222303#43222303
- Create your email-template.xml, may be copy from [EmailApplication/resources/templates/emails/sample-email-template.xml]
- load email templates from configured location as configured in application-*.yml
	- Take help of EmailApplication.sendAsyncEmailForSampleTemplate() and EmailApplication.sendSyncEmailForSampleTemplate()
	- EmailApplication.sendAsyncEmailForSampleTemplate()
		public void sendAsyncEmailForSampleTemplate() {
			EmailService emailService = appContext.getBean(DefaultEmailServiceImpl.class);
			emailService.init("name-of-email-template.xml", null, StringConstants.EMPTY_STRING_ARRAY);
			this.execute(emailService);
		}
	- EmailApplication.sendSyncEmailForSampleTemplate()
		public void sendSyncEmailForSampleTemplate() {
			EmailService emailService = appContext.getBean(DefaultEmailServiceImpl.class);
			emailService.init("name-of-email-template.xml", null, StringConstants.EMPTY_STRING_ARRAY);
			emailService.run();
		}
- If Overriding EmailService, the bean should have @Scope("prototype") as declared in DefaultEmailServiceImpl.java
- https://myaccount.google.com/lesssecureapps
	- enable "less secure app access" feature so that email sending works from gmail.
	- More Read: https://support.google.com/mail/answer/7126229?visit_id=637354211193070114-3402763138&rd=1#cantsignin
-----------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------
Features of Email Application:
- Cached emailVO loaded from template.xml
- Returning the Clone of emailVO
- Supports both Synchronous and Asynchronous email sending
-----------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------
Not Supported Features:
- email-template format
	- HTML Format is not supported yet
	- Attachment is not supported yet
-----------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------
TODO: Below
- email-template format
	- TODO: Support multiple <to> address: http://blog.bdoughan.com/2010/09/jaxb-collection-properties.html
- REST API expose to fetch list of cached emailVO and remove one/all from cache

More Info:
 - https://stackoverflow.com/questions/33123271/how-to-make-java-string-colored-when-sending-mail-using-simplemailmessage-class
	- The Spring 4.2.1 (http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/mail/SimpleMailMessage.html) documentation states, 
		that you should use MimeMessage instead of SimpleMailMessage if you want colors.
	- Limitation: <fromAddress>/<fromName> is not supported, configured username becomes fromAddress: https://stackoverflow.com/questions/32941673/senders-address-displayed-rather-than-his-name-while-sending-with-simplemailmes
