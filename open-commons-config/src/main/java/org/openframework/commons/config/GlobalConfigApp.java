package org.openframework.commons.config;

import java.util.Locale;

import org.openframework.commons.config.interceptor.AppConfigSecurityInterceptor;
import org.openframework.commons.config.servlet.GlobalConfigSecurityServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Configuration
@ServletComponentScan(basePackageClasses = { GlobalConfigSecurityServlet.class })
public class GlobalConfigApp implements WebMvcConfigurer {

	@Autowired
	private AppConfigSecurityInterceptor appConfigSecurityInterceptor;

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	/**
	 * https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/cookie-locale-resolver.html
	 * @return
	 */
	@Bean(name = "localeResolver")
	public LocaleResolver localeResolver() {

		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("en"));
		localeResolver.setCookieName("localeInfo");
		localeResolver.setCookieMaxAge(24*60*60);
		return localeResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(appConfigSecurityInterceptor).addPathPatterns("/secure/**");
		registry.addInterceptor(localeChangeInterceptor());
	}

    // https://attacomsian.com/blog/spring-boot-thymeleaf-layouts
    // http://www.javabyexamples.com/thymeleaf-multiple-template-locations-using-spring-boot
    // https://www.baeldung.com/spring-thymeleaf-template-directory

    @Bean
    @Profile("!dev")	// TODO: check if spring.thymeleaf.prefix is absent
	public ClassLoaderTemplateResolver primaryTemplateResolver() {
		ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
		secondaryTemplateResolver.setPrefix("META-INF/resources/webjars/global-config/templates/");
		secondaryTemplateResolver.setSuffix(".html");
		secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
		secondaryTemplateResolver.setCharacterEncoding("UTF-8");
		secondaryTemplateResolver.setOrder(1);
		secondaryTemplateResolver.setCheckExistence(true);

		return secondaryTemplateResolver;
	}

    @Bean
    @Profile("dev")	// TODO: check if spring.thymeleaf.prefix is absent
	public ClassLoaderTemplateResolver secondaryTemplateResolver() {
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

	@Bean
	public ViewResolver resourceBundleViewResolver() {
	    ResourceBundleViewResolver bean = new ResourceBundleViewResolver();
	    bean.setBasename("views");
	    bean.setOrder(1);
	    return bean;
	}

	/**
	 * This is a limitation that the InternalResourceViewResolver will be the
	 * default view resolver for the app using this config framework.
	 * 
	 * @return InternalResourceViewResolver
	 */
	/*
	 * @Bean public InternalResourceViewResolver jspViewResolver() {
	 * InternalResourceViewResolver bean = new InternalResourceViewResolver();
	 * bean.setPrefix("/WEB-INF/jsp/"); bean.setSuffix(".jsp"); bean.setOrder(1000);
	 * return bean; }
	 */

	/**
	 * This code isn't required in spring boot app, as it is automatically done by spring boot framework.
	 */
	/*
	 * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 * //registry.addResourceHandler("/resources/**").addResourceLocations(
	 * "/resources/");
	 * //registry.addResourceHandler("swagger-ui.html").addResourceLocations(
	 * "classpath:/META-INF/resources/");
	 * //registry.addResourceHandler("/webjars/**").addResourceLocations(
	 * "classpath:/META-INF/resources/webjars/"); }
	 */

}

