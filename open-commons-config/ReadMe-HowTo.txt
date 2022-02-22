How to use Config Application:
- Create Spring Boot Project (only for non open-parent projects)
- Add the following dependency in pom.xml
		<dependency>
			<groupId>org.openframework</groupId>
			<artifactId>open-commons-config</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
		<dependency>
			<groupId>org.apache.tomcat.embed</groupId>
			<artifactId>tomcat-embed-jasper</artifactId>
			<scope>provided</scope>
		</dependency>
- Scan the beans defined in the config project by adding package as below::
	@SpringBootApplication(scanBasePackageClasses = { TestAppApplication.class, GlobalConfigApp.class })
- application.properties: add context-path [server.servlet.context-path=/demoApp]
- Build the project
- mvn clean install
- Run the app with VM args [-DsharedPath=C:\demo] OR by Adding the config files in src/main/resources directory
	- alternatively the config files can be kept outside the classpath, but the location needs to be given as argument to program as below:
		> java -jar spring-boot-executable.jar -DAPP_HOME=C:\appHome -Dspring.profiles.active=dev
-----------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------
- Why tomcat-jasper dependency is added?
	- Check Here: https://www.websparrow.org/spring/jsp-page-file-is-not-rendering-in-spring-boot-application#:~:text=All%20we%20know%2C%20Spring%20Boot,in%20your%20Spring%20Boot%20application.
- 