How to use Config Application:
- Add the following dependency in pom.xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.tomcat</groupId>
			<artifactId>tomcat-jasper</artifactId>
			<version>9.0.1</version>
		</dependency>
		<dependency>
			<groupId>org.openframework</groupId>
			<artifactId>open-commons-config</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
- Scan the beans defined in the config project by adding package as below::
	@SpringBootApplication(scanBasePackageClasses = GlobalConfigSpringMvcWebConfig.class)
- Add the config files in src/main/resources directory
	- alternatively the config files can be kept outside the classpath, but the location needs to be given as argument to program as below:
		> java -jar spring-boot-executable.jar -DsharedPath=C:\configLocation
-----------------------------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------------------
- Why tomcat-jasper dependency is added?
	- Check Here: https://www.websparrow.org/spring/jsp-page-file-is-not-rendering-in-spring-boot-application#:~:text=All%20we%20know%2C%20Spring%20Boot,in%20your%20Spring%20Boot%20application.
- 