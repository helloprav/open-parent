https://spring.io/blog/2020/04/30/updates-to-spring-versions

This parent pom project should have all sub modules that can be plugged into any spring/spring-boot project.
It should be developer friendly to use any module from this parent project.
Typically the followings can be order of steps to use any module from this parent project:
	1) Use maven dependency in your spring (or spring boot) project's pom.xml
	2) Use top level class of the module on SpringBoot main method as following annotation:
		@SpringBootApplication(scanBasePackageClasses = { NeerAppApplication.class })
	3) Add application-<module>.yml (or any other name for the yml/properties file) in classpath. Optional step
	4) Start using the classes from this added module

test changes v2

=====================================================================
2023-07-XX
------------------------------------------------------------
- Use plugin management similar to dependency management in all pom.xml.
	- https://www.baeldung.com/maven-plugin-management
	- https://www.baeldung.com/maven-copy-files
- Use npm package manager in all UI related modules. They should store the node_module in root/context level (except OFDS module)
- use common layout template file for all the web(UI) modules.
- Top menus in the layout file to be fixed (may be coming from rest api. all web modules to register themselves and the registered list to be returned to be displayed as top menu)
- Sub menus against a top menu to be coming from the module specific rest API. Try to use HATEOAS. Test this against the pariksha/module of gurukul
- Replace all SOP with loggers
- Logout button
- FontAwesome or any other font: use latest font for maximum icons types
- Config App: Replace JSP with Thymeleaf completely
