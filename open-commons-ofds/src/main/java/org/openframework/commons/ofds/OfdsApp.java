package org.openframework.commons.ofds;

import java.util.List;

import org.openframework.commons.ofds.controller.argumentresolver.UserProfileHandlerMethodArgumentResolver;
import org.openframework.commons.ofds.controller.interceptor.OfdsSecurityInterceptor;
import org.openframework.commons.ofds.props.MasterDataProps;
import org.openframework.commons.ofds.props.OfdsSecurityProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({ OfdsSecurityProps.class, MasterDataProps.class })
@EnableJpaRepositories(basePackages = "org.openframework.commons.ofds")
@EntityScan(basePackageClasses = OfdsApp.class)
public class OfdsApp implements WebMvcConfigurer {

	@Autowired
	private UserProfileHandlerMethodArgumentResolver userProfileHandlerMethodArgumentResolver;

	@Autowired
	private OfdsSecurityInterceptor securityInterceptor;

	/**
	 * More read on addPathPatterns()
	 * http://opensourceforgeeks.blogspot.com/2016/01/difference-between-and-in-spring-mvc.html
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(securityInterceptor).addPathPatterns("/**/ofds/api/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(userProfileHandlerMethodArgumentResolver);
	}

	/**
	 * The following works in Spring-Boot Env as ServletRegistrationBean belongs to
	 * boot package. For simple spring app (non Spring Boot), SecurityServlet should
	 * be registered automatically using annotations.
	 * 
	 * @return ServletRegistrationBean<Servlet>
	 */
	/*@Bean
	public ServletRegistrationBean<Servlet> configServletRegistrationBean() {
		ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<>(new SecurityServlet());
		registration.addUrlMappings("/app");
		registration.addUrlMappings("/app/*");
		return registration;
	}*/

	/*
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addRedirectViewController("/docApi/v2/api-docs", "/v2/api-docs");
	    registry.addRedirectViewController("/docApi/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
	    registry.addRedirectViewController("/docApi/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
	    registry.addRedirectViewController("/docApi/swagger-resources", "/swagger-resources");
	}

	//@Override
	public void addResourceHandlers1(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/docApi/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
	    registry.addResourceHandler("/docApi/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}*/

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
