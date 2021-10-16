package org.openframework.commons.config;

import java.util.Locale;

import org.openframework.commons.config.interceptor.AppConfigSecurityInterceptor;
import org.openframework.commons.config.servlet.GlobalConfigSecurityServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;


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
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		//registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
