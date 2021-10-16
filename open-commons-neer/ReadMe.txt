2020-12-xx
------------------------------------------------------------
Items Come Here
- Sub Items come here
- Filtering: allow to filter entities based on filter criteria  
	######################################################################################
	https://www.google.com/search?q=spring+data+jpa+criteria+builder+example&rlz=1C1GCEA_enIN821IN822&source=lnt&tbs=cdr%3A1%2Ccd_min%3A8%2F1%2F2017%2Ccd_max%3A8%2F6%2F2019&tbm=
	https://www.baeldung.com/jpa-pagination
	https://www.baeldung.com/spring-data-criteria-queries
	######################################################################################
- Distributed Cache: https://dzone.com/refcardz/getting-started-infinispan?chapter=1
	- https://docs.huihoo.com/jboss/A-memcached-implementation-in-Java.pdf
- update auth-service.js: $http returns promise, so its difficult
- Angular directive for column sorting html: not possible as sorting image (up/down) not updating
- log4j2 with slf4j - https://howtodoinjava.com/spring-boot2/logging/spring-boot-logging-configurations/
============================================================
Spring Boot Payment Integration with Paytm
 - https://www.youtube.com/watch?v=LWv1_CYr3dc&t=19s&fbclid=IwAR2uT3LVrpU8aVt-I6T9Jqu-m2k1ukWFWCI6W0BFL-7wC96w0ykpvVaZYeM
Send SMS On Mobile Using Java
 - https://www.youtube.com/watch?v=NaLbk6czntg&feature=youtu.be&fbclid=IwAR0fl4mmHBbIwyqZ5FhXUPo-uLSWYsvOwH6ggYZaBOm6GMyDCuVs6_2MCIg
Generate OTP (One Time Password) Using Java 8
 - https://www.youtube.com/watch?v=5rOAfRG4vDw
============================================================
Rules Framework/Engine:
- Implement the best rules framework as open-commons-rules module
============================================================
Report Generation:
- Export To Excel: All tabular records
- Print To PDF: Provide this on almost all Pages.
============================================================
2020-12-xx
------------------------------------------------------------
- REST API Navigation
	- HATEOAS Implementation (use chrome: Praveen profile)
		- good one: https://www.youtube.com/watch?v=vvcANMpfr5I
		- example: https://github.com/TechPrimers/rest-hateoas-example/blob/master/src/main/java/com/techprimers/rest/resthateoas/resource/UsersResource.java
	- Other links:
		- https://restfulapi.net/hateoas/
		- https://symfonycasts.com/screencast/rest-ep2/pagination
		- https://www.baeldung.com/rest-api-pagination-in-spring
		- https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
		- https://www.javadevjournal.com/spring/rest-pagination-in-spring/
		- http://appsdeveloperblog.com/rest-pagination-tutorial-with-spring-mvc/
		- https://dzone.com/articles/conditional-pagination-and-sorting-using-restful-w
		- https://dzone.com/articles/rest-pagination-spring
		- https://www.baeldung.com/rest-api-discoverability-with-spring
		- https://www.baeldung.com/restful-web-service-discoverability
		- https://howtodoinjava.com/resteasy/writing-restful-webservices-with-hateoas-using-jax-rs-and-jaxb-in-java/
		- https://howtodoinjava.com/spring-boot/rest-with-spring-hateoas-example/
		- https://www.baeldung.com/spring-hateoas-tutorial
		- https://docs.spring.io/spring-hateoas/docs/current/reference/html/
=====================================================================
2020-03-xx
------------------------------------------------------------
All Topics
- Config Framework: make config-app based on configuration (use yaml for url, cookie-name, etc.)
- Error/Validation: All error validation message based on localization
============================================================
2020-05-xx
------------------------------------------------------------
UI
- Success/Error message system
============================================================
2020-05-xx
------------------------------------------------------------
Refactor Code
- Check all user/group implementation: service/junit/postman/ui
- change package of config-stub from config to configstub
- Create Entity should return all the details of created entity?
	- https://stackoverflow.com/questions/12806386/standard-json-api-response-format
	- https://jsonapi.org/
	- https://jsonapi.org/format/
- verify all attributes (including checker/maker) for user/group after it is created/updated using 
	- getById(id)
	- convert json response into VO and verify checker/maker
- 
https://howtodoinjava.com/spring-boot2/sb-multi-module-maven-project/
=====================================================================
2020-01-xx
------------------------------------------------------------
- Convert Project/ReadMe.txt file into README.md
=====================================================================
2020-09-xx
------------------------------------------------------------
- Enums may be moved to some more specific commons jar.
	- Try replacing hard coded enums with generic solution so that app1 has RelationType.4 and app2 has RelationType.6 values
- Create new module for Neer app's functionality... like eshop or catalogue
=====================================================================
2020-10-xx
------------------------------------------------------------
- ofds
	- name of app=>ofds to be in single place for each of (app.js/AppConstants.java)
	- ofds/app.js to be similar to gconfig/app.js
- AOP Aspect: Performance
	- log time in ms
	- enable based on configuration
- Versioning of Module:
	- Upgrade the release version from <version>0.0.1-SNAPSHOT</version> to <version>0.0.x-SNAPSHOT</version>
	- ui to manage lifecycle of cache??
	- help from alcs??
=====================================================================
2020-10-xx
------------------------------------------------------------
- ofds
	- Download tomcat logs using the gconfig app (https://www.baeldung.com/spring-boot-embedded-tomcat-logs)
=====================================================================
2021-04-xx
------------------------------------------------------------
-Modular Unit & Integration Testing
	https://reflectoring.io/spring-boot-modules/
	https://reflectoring.io/spring-boot-application-events-explained/
	https://reflectoring.io/spring-boot-test/
	https://reflectoring.io/unit-testing-spring-boot/
	https://reflectoring.io/spring-boot-web-controller-test/
	https://reflectoring.io/testing-verticals-and-layers-spring-boot/
=====================================================================
2020-10-xx
------------------------------------------------------------
- Spring Boot Events
	- Publish event in OFDS & Listen in email module to sendEmail()
=====================================================================
2021-03-14
------------------------------------------------------------
- Moved Neer app inside open-parent as a module
	- all versions in properties
=====================================================================
2020-11-02
------------------------------------------------------------
- Cache Management:
	- Caffeine cache: for email template caching
=====================================================================
2020-11-01
------------------------------------------------------------
- db migation using flyway. Generated sql scripts for 
	- create main and history tables
	- triggers
	- master-data
	- https://www.baeldung.com/database-migrations-with-flyway
	- https://dzone.com/articles/build-a-spring-boot-app-with-flyway-and-postgres
=====================================================================
2020-10-29
------------------------------------------------------------
- Cache Management:
	- New Maven jar project: Core Module:: https://reflectoring.io/spring-boot-starter/
	- Register all cache ::: individual cache names or all through application.yml
	- CacheController rest api to fetch and evict caches
=====================================================================
2020-10-10
------------------------------------------------------------
- Config App update features
	- re-order left menu
	- name of app=>config-app. app name to be in single place for each of (app.js/AppConstants.java)
	- Security in Config-App Project. Read basic credentials from cred.properties file
		- in spring boot app, if *.properties is kept in classpath of dependent jar, then it can't be loaded by application.
		- so in above case, it is better to keep the *.properties file in main app's classpath instead of dependent jar's classpath.
		- views.properties file is an exception which works somehow from dependent jar's classpath
	- make it sub-moduler on @Controller level
=====================================================================
2020-10-03
------------------------------------------------------------
- Update Config-App Project for showcasing IDEA
- actuator related properties to be separated from application-<profile>.yml to application-<profile>-actuator.yml
- ofds api url should have prefix of 'ofds/'
- Email Utility
	- organize the email's config files in neer app
=====================================================================
2020-10-02
------------------------------------------------------------
- OFDS App
	- /contextName/ofds will redirect to ofds app ui
	- ofds app context path corrected. logout redirect page working.
	- localization/translation service of angularjs working.
	- language dropdown appears on ui/menu
=====================================================================
2020-09-29
------------------------------------------------------------
- Email Utility
	- should support optional email enable/disable functionality jsut by changing configuration
		- e.x. User login sends email which can be disabled
- Ofds module should be up and running
	- wisely use the word 'ofds'
		- ofds static url format: http://<server-name>:<port>/<context-name>/ofds/#/user-mgmt/users/list/
	- BaseVO may be in sync with AbstractCommonEntity by renaming either of two.
	- Move files of this ofds module to more generic module (utils, constants, props etc)
	- security related properties to be separated from application-<profile>.yml to application-<profile>-security.yml
		- root element to be ofds.security
=====================================================================
2020-03-10
------------------------------------------------------------
Performance
- Spring Boot Actuator
============================================================
2020-01-08
------------------------------------------------------------
- Updated the npm_install.bat to keep module's version number as well
- Removed DEFAULT locale for message and config properties of dev/prod env
- Shopping Cart APIs: http://api.opencart-api.com/demo/shopping-cart/#/
- http://test.opencart-api.com/rest_admin_api/#!/manufacturer
- https://opencart-api.com/opencart-rest-api-documentations/
=====================================================================
2020-03-07
------------------------------------------------------------
- Show/Download logs from config module
- Change log level functionality: dynamically show list of Logger configured in logback.xml [http://mailman.qos.ch/pipermail/logback-user/2008-November/000751.html]
- Use either slf4j or logback api to update the log levels [move logback code into LogbackUtils.java]
=====================================================================
2020-03-01
------------------------------------------------------------
- Load application-*.xml files from outside jar
	- Update pom.xml to copy all necessary application-*.xml files into may be /config folder outside ofds.jar
- Load logback.xml (and other resources if any) from extrenal classpath
	- http://www.codevomit.xyz/bootlog/blog/how-to-provide-spring-boot-fat-jar
- Display System Properties on config module
- Display System Beans on config module
=====================================================================
2020-02-17
------------------------------------------------------------
- LoggingAspect
	- separate maven jar project (commons-aop)
	- annotations in the commons-aop project
	- Generate Logs using Spring AOP
- ApplicationContextProvider in Spring Utils project
=====================================================================
2020-01-26
------------------------------------------------------------
- Send email feature
	- separate maven jar project (commons-email)
	- Have EmailVO to store all the email related data (from, to, subject, body etc)
	- XML templates to store the email data to be converted into EmailVO
	- Pass this emailVO to emailService.sendEmail() of email project
	- EmailService to be interface. Provide EmailServiceImpl
	- Provide default values to properties in email/application.properties
		- The default values to be over-ridden in ofds/resources/application-email.properties
		- For above: https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config
	- Make Commons-Email project fully pluggable. 
		- Include dependency, 
		- Add EmailApplication.class in @SpringBootApplication OfdsApplication
	- Create a new thread for sending email so that main feature is not blocked.
=====================================================================
2020-01-12
------------------------------------------------------------
- prepare a list of what all should be tested after every release
	- junit tests
	- Swagger docs
	- config module
	- admin status (db connection, email status, etc)
	- Send email feature
	- Postman Tests
============================================================
2020-01-12
------------------------------------------------------------
Database
- Automation of Maintain history Tables
	- Build and deploy application from fresh. Following sequence to be used:
		- Done (bin\ofds-maven-build.bat) - Build and Deploy Spring Boot App
		- Done (Manual Step) - Copy mysql db folder into distribution-root-directory.
		- Done (bin\db-init.bat) - Create Database Server
		- Done (bin\ofds-start.bat) - Start Spring Boot App.
		- Done: Create MasterData ONLY for admin_status
		- Done (bin\db-objects-create-script.bat) Create Triggers Script
			- Script to be triggered manually to some automation
			- but not from Java code as it has many issues and wastes time without much significant outcome.
		- Done: (URL: /admin/create-master-data) Create MasterData for other tables (user, group, function etc)
		- Done: Create New Interceptor for OFDS Admin Console to create master data before any other action
============================================================
2019-12-25
------------------------------------------------------------
Database
- Maintain history Tables 
	- Issues with Using JPA Auditing: (https://www.baeldung.com/database-auditing-jpa)
		- Doesn't support auditing in case of bulk upload
		- Doesn't support auditing when using @Query annotation along with @Modifying which is used for Patch (update IS_VALID status)
	- Using database Trigger for auditing. Below steps if want to maintain history of new entity.
		- DB Design as Follows
			- GroupHistoryVO should have same properties as GroupVO
			- GroupHistoryVO extends HistoryVO extends BaseVO
			- GroupHistory should have same properties as Group
			- GroupHistory extends AbstractHistoryEntity extends AbstractCommonEntity
		- Create INSERT & UPDATE Triggers for new Entity
		- Create other supporting classes/methods.
		- Write Unit Tests for fetching History data.
============================================================
2019-09-17
------------------------------------------------------------
REST Documentation
- Use Swagger for REST Docs: https://howtodoinjava.com/swagger2/swagger-spring-mvc-rest-example/
    - SwaggerConfigFile.java
    - springfox swagger dependency. also add following:
			<dependency>
				<groupId>org.springframework.plugin</groupId>
				<artifactId>spring-plugin-core</artifactId>
				<version>1.2.0.RELEASE</version>
			</dependency>
	- URLs:
		http://localhost:9080/ofds/swagger-ui.html
		http://localhost:9080/ofds/v2/api-docs
============================================================
2019-09-17
------------------------------------------------------------
- https://github.com/cryptlex/rest-api-response-format
- https://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api
Paging & Sorting
- Paging and Sorting support in REST API: Accept pagination parameters
- Return total items and other page details in REST Response
- Sorting: Spring data jpa supports different sorting order on different columns.
- Return all response for a specific pagination request (may be limit=-1 or limit=0) || (limit < 1)
- reset page no =1, if limit < 1; reset page no =1, if page no <1;
- Paging and Sorting support in REST Client (
	- NEW Article: https://angular-ui.github.io/bootstrap/#!#pagination
	- OLD Article: https://dzone.com/articles/server-side-pagination-using-angularjs-web-api-and
============================================================
2019-07-19
------------------------------------------------------------
- Eclipse Error in Spring boot [javax.net.ssl.SSLException; MESSAGE: closing inbound before receiving peer's close_notify]
- refactor/arrange methods in controller/service/impl/repository by order of use case
- Complete Use Case Change (isValid) Status of User/Group for RESTService, JUnit, Postman, UI
	- Reference: https://thoughts-on-java.org/common-hibernate-mistakes-cripple-performance/
- Log message formatting: 
	- Client IP in logs
	- https://reflectoring.io/logging-format-logback/
	- https://logback.qos.ch/manual/layouts.html
- Super Admin to have all the available access
- Move utility classes to more general package (i.e. commons) instead of keeping it in ofds or common-rest or common-config package
	- Encryption and Hashing in Commons/Utils JAR
=====================================================================
2019-06-16
------------------------------------------------------------
- create/update user/group serviceASImpl should have common code
	- create/update/find group: complete junit, update postman tests
- user/group checker/maker completed.
- Use JPA's entityManager to create master data by reading from yml file
- Delete entity use case implement for Service/JUnit/Postman
- Delete the entity after created and tested, to restore db in same original state
- Write JUnit for update user/group details
	- Update User/Group Completed.
	- https://howtodoinjava.com/hibernate/merging-and-refreshing-hibernate-entities/
- https://cloud.google.com/blog/products/gcp/12-best-practices-for-user-account
============================================================
2019-05-29
------------------------------------------------------------
- For Create User getAllGroups on UI, same as create Group
- Create/Retrieve/Update for User & Group completed with Service/Junit/UI-Integration
============================================================
2019-05-26
------------------------------------------------------------
- Update JUnit for common tests like:
	.andExpect(jsonPath("$.data[0].isValid", is(true)))
	.andExpect(jsonPath("$.data[0].id").exists())
- Update for getUsers, getUsersByStatus, getUsersById 
- Show all fields on UI for getUserById
- Postman validation like Junit (exists(), hasJsonPath(), isBoolean() etc)
- Update user: don't update password to NULL. 
- created date and modified dates are not correct (check time zone)
============================================================
2019-05-16
------------------------------------------------------------
- Install node_module from package.json tested ok [just type: npm install]
- integrate config module (/app) to GUI. Permission to Super Admin Group
- Clean JS / HTML
	- remove un-used files from proj
	- move files to correct layer (config, controller, service)
============================================================
2019-05-15
---------------------------------------------------------------------
GUI Internationalization
- Show language dropdown (load from REST Service)
- Switch Language
- Updated REST api to load the messages for given language
- AngularJS Translation 
	- Instalation (https://angular-translate.github.io/docs/#/guide/00_installation)
	- Loading multi lingual message (https://gist.github.com/johnlogsdon/11185943)
	- YouTube Video (https://www.youtube.com/watch?v=wRFhnu0friY)
==========================================================================
2019-05-12
---------------------------------------------------------------------
AngularJS UI Update for
- Users list in table
- Client Side Sorting, Pagination, Page Size dropdown, Filtering a column
- For Pagination: http://plnkr.co/edit/xmjmIId0c9Glh5QH97xz?p=preview
==========================================================================
2019-05-11
------------------------------------------------------------
Postman
- https://learning.getpostman.com/docs/postman/scripts/test_examples
- JSON Response Validation in Postman
- Re-Usable Validation
JUnit Tests
- BaseControllerTest to have common test steps
- Common tests (statusCode, error/success json elements)
- Response Data validation
Auth/Logout + Find All Users + Find User By Status
- Rest Service / JUnit
- Postman request created for auth/logout
- UI Updated for logout
============================================================
2019-05-05
------------------------------------------------------------
Entity Changes
- Role & admNo fields removed
- isSuperAdmin field added
Spring Boot Static Project Integration
- NPM install - one by one
- UI moved to src/main/resources/static
Authentication/Login user story implemented with
- AuthenticationControllerTest for login
- UI Updated for error message
- postman collection folder created for auth/login
- Language Support for Hindi & Bangla
============================================================
2019-04-26
------------------------------------------------------------
Spring Boot Multiple View Resolver Support
- ResourceBundle for Config App
- Thymeleaf for JSP like Dynamic Content on HTML
- src/main/resources/static to have SPA AngularJS app
- Request URL & Logged In User ID in each log
============================================================
2019-04-14
------------------------------------------------------------
Spring Boot Deployable App 
- https://dzone.com/articles/packaging-springboot-application-with-external-dep
- Database Setup Guide Taken From: https://www.youtube.com/watch?v=kaxrn_n7Jsg
============================================================
2019-04-08
------------------------------------------------------------
Spring Boot Config Framework
- Dynamically Update Configuration in Spring Boot
	- All config-app related dependency moved into commons-config
	- remove unused dependency from ofds app
	- removed tiles3 dependency (use jsp:include tag)
	- WebConfig to OfdsWebConfig
	- 
============================================================
2019-03-13
------------------------------------------------------------
SecurityInterceptor => AuthenticationInterceptor
- Allow test/junit to call unprotected and NOT unprotected urls (done using: securityProps.getUnprotectedUrls().add(REST_URL))
- Allow REST API call unprotected and NOT unprotected urls (done using: initUserProfile(request))
- Pass the User profile as parameter to RestController methods (done using: initUserProfile(request) and UserProfileHandlerMethodArgumentResolver)
- Removed AbstractUserProfileHandlerMethodArgumentResolver.class
- Included Open-Parent project for changes and to be saved as new repo like ofds
============================================================
2019-03-10
------------------------------------------------------------
TODO: Maintain history data for almost all tables:
- Update XLS[DB Model] design of All Entities History Tables.
- Check if JPA Events will work or Trigger is required.
- Implement history of data
============================================================
2019-03-07
------------------------------------------------------------
DB Design For Entities:
- Removed unused entity: Address.java, UserAddress.java, UserRelative.java, UserRole.java
- Updated the design of User (removed Role property)
- Updated JUnit accordingly
- Removed the findUsersByRoleAndStatus() from UserController
- Updated XLS[P1 - DB Model] design of All Entities User, Group, Function and mappings.
- Updated XLS[UseCases] Authentication, Group Management, User Management, User Profile / Management.
- Updated XLS[RESTServices] Authentication, and Other section in this sheet.
============================================================
2019-03-06
------------------------------------------------------------
Infrastructure related tasks completed
- Spring Boot MVC Framework Completed
- slf4j/logback completed
- Unit Testing using JUnit 5 Testing Framework Completed
============================================================
