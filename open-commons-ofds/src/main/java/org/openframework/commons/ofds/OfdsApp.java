package org.openframework.commons.ofds;

import java.util.List;

import javax.annotation.PostConstruct;

import org.openframework.commons.config.constants.ConfigAppConstants;
import org.openframework.commons.ofds.controller.argumentresolver.UserProfileHandlerMethodArgumentResolver;
import org.openframework.commons.ofds.controller.interceptor.OfdsApiSecurityInterceptor;
import org.openframework.commons.ofds.props.MasterDataProps;
import org.openframework.commons.ofds.props.OfdsSecurityProps;
import org.openframework.commons.utils.FileFolderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({ OfdsSecurityProps.class, MasterDataProps.class })
@EnableJpaRepositories(basePackages = "org.openframework.commons.ofds")
@EntityScan(basePackageClasses = OfdsApp.class)

//@EnableJpaRepositories("org.openframework.commons")
//@EntityScan("org.openframework.commons")
//@AutoConfigurationPackage(basePackages = {"org.openframework.commons"}) 
public class OfdsApp implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(OfdsApp.class);

	@PostConstruct
	public void validateEnvVariables() {
		String appHome = System.getProperty(ConfigAppConstants.ARGS_APP_HOME);
		logger.info(ConfigAppConstants.ARGS_APP_HOME+" from environment variable: "+appHome);
		if(!FileFolderUtils.isDirectoryExists(appHome)) {
			logger.error(ConfigAppConstants.ARGS_APP_HOME + " from environment variable does not exists.");
			throw new Error(ConfigAppConstants.ARGS_APP_HOME + " from environment variable does not exists.");
		}
	}

	@Autowired
	private UserProfileHandlerMethodArgumentResolver userProfileHandlerMethodArgumentResolver;

	@Autowired
	private OfdsApiSecurityInterceptor securityInterceptor;

//	@Autowired
//	private AuthorizationRequestInterceptor authorizationRequestInterceptor;

	/**
	 * More read on addPathPatterns()
	 * http://opensourceforgeeks.blogspot.com/2016/01/difference-between-and-in-spring-mvc.html
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(securityInterceptor).addPathPatterns("/ofds/api/**").order(Ordered.HIGHEST_PRECEDENCE);
		//registry.addInterceptor(authorizationRequestInterceptor).addPathPatterns("/*/api/**");
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
