This parent pom project should have all sub modules that can be plugged into any spring/spring-boot project.
It should be developer friendly to use any module from this parent project.
Typically the followings can be order of steps to use any module from this parent project:
	1) Use maven dependency in your spring (or spring boot) project's pom.xml
	2) Use top level class of the module on SpringBoot main method as following annotation:
		@SpringBootApplication(scanBasePackageClasses = { NeerAppApplication.class })
	3) Add application-<module>.yml (or any other name for the yml/properties file) in classpath. Optional step
	4) Start using the classes from this added module

test changes v2
