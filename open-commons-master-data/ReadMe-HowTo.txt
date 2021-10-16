How to use MasterData Application:
- Add the dependency in pom.xml
- Scan the beans defined in this MasterData project by adding package [org.openframework.commons.masterdata] or Class (MasterDataApp.class) to scan.
	- e.g. Add MasterDataApp.class in @SpringBootApplication NeerApplication
- To load the MasterData into database table, do following:
	- Create a copy of [MasterDataApplication/resources/application-master-data.yml] file and name it application-<profile->master-data.yml
	- Save above application-<env->master-data.yml file in your spring boot app's classpath
	- Override the properties in above file as applicable.
	- Add the <profile>-master-data into application's as shown below:
		spring:
		  profiles:
    		include: dev-jdbc, master-data
	- Refer: https://stackoverflow.com/questions/35663679/spring-boot-inherit-application-properties-from-dependency/43222303#43222303
