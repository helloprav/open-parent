package org.openframework.commons.config;

import java.time.Duration;
import java.util.Locale;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@ServletComponentScan(basePackageClasses = { GlobalConfigApp.class })
public class GlobalConfigApp implements WebMvcConfigurer {

	//@Autowired
	//private AppConfigSecurityInterceptor appConfigSecurityInterceptor;

    @Bean
    LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

    /**
     * https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/cookie-locale-resolver.html
     * @return
     */
    @Bean(name = "localeResolver")
    LocaleResolver localeResolver() {

		CookieLocaleResolver localeResolver = new CookieLocaleResolver("localeInfo");
		localeResolver.setDefaultLocale(new Locale("en"));
		localeResolver.setCookieMaxAge(Duration.ofHours(24));
		return localeResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(appConfigSecurityInterceptor).addPathPatterns("/secure/**");
		registry.addInterceptor(localeChangeInterceptor());
	}

    // https://attacomsian.com/blog/spring-boot-thymeleaf-layouts
    // http://www.javabyexamples.com/thymeleaf-multiple-template-locations-using-spring-boot
    // https://www.baeldung.com/spring-thymeleaf-template-directory

    // TODO: check if spring.thymeleaf.prefix is absent
    @Bean
    @Profile("!dev")
    ClassLoaderTemplateResolver primaryTemplateResolver() {
		ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
		secondaryTemplateResolver.setPrefix("META-INF/resources/webjars/global-config/templates/");
		secondaryTemplateResolver.setSuffix(".html");
		secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
		secondaryTemplateResolver.setCharacterEncoding("UTF-8");
		secondaryTemplateResolver.setOrder(1);
		secondaryTemplateResolver.setCheckExistence(true);

		return secondaryTemplateResolver;
	}

    // TODO: check if spring.thymeleaf.prefix is absent
    @Bean
    @Profile("dev")
    ClassLoaderTemplateResolver secondaryTemplateResolver() {
		ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
		secondaryTemplateResolver.setPrefix("file:src/main/resources/templates/");
		secondaryTemplateResolver.setSuffix(".html");
		secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
		secondaryTemplateResolver.setCharacterEncoding("UTF-8");
		secondaryTemplateResolver.setCacheable(false);
		secondaryTemplateResolver.setOrder(1);
		secondaryTemplateResolver.setCheckExistence(true);

		return secondaryTemplateResolver;
	}
}

