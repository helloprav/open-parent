package org.openframework.commons.neer;

import java.util.Arrays;

import org.openframework.commons.aop.CommonsLoggingAspect;
import org.openframework.commons.cache.EnableCommonsCaching;
import org.openframework.commons.config.GlobalConfigApp;
//import org.openframework.commons.email.EmailApplication;
//import org.openframework.commons.email.service.DefaultEmailServiceImpl;
//import org.openframework.commons.email.service.EmailService;
//import org.openframework.commons.email.vo.EmailVO;
import org.openframework.commons.encrypt.EncryptionUtil;
import org.openframework.commons.jpa.JpaConfig;
import org.openframework.commons.ofds.OfdsApp;
import org.openframework.commons.rest.advice.RestResponseBodyAdvice;
import org.openframework.commons.shiksha.ShikshaConfiguration;
import org.openframework.commons.spring.utils.ApplicationContextProvider;
import org.openframework.commons.spring.utils.SpringUtils;
import org.openframework.commons.swagger.OpenApiConfiguration;
import org.openframework.commons.utils.SystemUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/*
 * Sending email is disabled by GMail: https://support.google.com/mail/thread/166547017/access-to-gmail-through-third-party-apps-or-devices?hl=en
 */
@EnableCommonsCaching
@SpringBootApplication(scanBasePackageClasses = { NeerApplication.class, CommonsLoggingAspect.class,
		GlobalConfigApp.class, EncryptionUtil.class, RestResponseBodyAdvice.class,
		ApplicationContextProvider.class, OpenApiConfiguration.class, OfdsApp.class, ShikshaConfiguration.class, JpaConfig.class })
public class NeerApplication {

	private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
	private static final String LOG_LEVEL = "LOG_LEVEL";

	public static void main(String[] args) {

//		System.out.println("Profile: " + System.getProperty(SPRING_PROFILES_ACTIVE));
//		System.out.println("LOG_LEVEL: " + System.getProperty(LOG_LEVEL));
		ConfigurableApplicationContext obj = SpringApplication.run(NeerApplication.class, args);
//		System.out.println("Profile: " + System.getProperty(SPRING_PROFILES_ACTIVE));
		String printDetails = System.getProperty("printDetails");
		if(null != printDetails) {

			SpringUtils.getSpringBeans();
			SpringUtils.printOtherDetails();
			SystemUtils.printSystemProperties();
			System.out.println(obj);
			System.out.println("OFDS Application Started Successfully with the Profile: "
					+ System.getProperty(SPRING_PROFILES_ACTIVE));
			System.out.println("spring.config.location: " + System.getProperty("spring.config.location"));
			System.out.println("LOG_LEVEL: " + System.getProperty(LOG_LEVEL));
			System.out.println("spring.config.name: " + System.getProperty("spring.config.name"));

			scanSpringBeansOfCache();
			scanSpringBeansOfCache();
			//sendTestEmail();
			SpringUtils.printServerDetails();
		}
	}

	public static void scanSpringBeansOfCache() {

		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		String[] beanNames = appContext.getBeanNamesForType(Cache.class);
		Arrays.parallelSort(beanNames);

		for (String beanName : beanNames) {
			System.out.println("beanName: "+beanName);
			Cache cache = (Cache)appContext.getBean(beanName);
			scanCache(cache);
		}
	}

	private static void scanCache(Cache cache) {

		System.out.println(cache.getName());
	}

	/*
	public static void sendTestEmail() {
		EmailService emailService = ApplicationContextProvider.getApplicationContext().getBean(DefaultEmailServiceImpl.class);
		EmailVO emailVO = new EmailVO();
		emailVO.addTo("helloprav@gmail.com");
		emailVO.setSubject("subject");
		emailVO.setBody("body");
		emailVO.setFromAddress("praveenatwork2017@gmail.com");
		emailService.sendSimpleMail(emailVO);
	}	*/

}

//https://reflectoring.io/categories/spring-boot/page/2/
//try this:: https://reflectoring.io/spring-boot-starter/
//https://reflectoring.io/spring-boot-modules/
//https://reflectoring.io/categories/spring-boot/
//https://www.baeldung.com/circular-dependencies-in-spring
//https://reflectoring.io/spring-boot-cache/
//https://reflectoring.io/spring-boot-application-events-explained/
//https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2
//http://coders-kitchen.com/2016/11/01/spring-boot-multi-module-projects-adding-module-specific-property-files/
// Using @Bean in @Configuration vs @Component 
//configuration-processor
