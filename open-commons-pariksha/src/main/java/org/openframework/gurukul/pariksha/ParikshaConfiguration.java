package org.openframework.gurukul.pariksha;

import org.openframework.commons.ofds.controller.interceptor.OfdsWebSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@EnableJpaRepositories(basePackages = "org.openframework.gurukul.pariksha")
@EntityScan(basePackageClasses = ParikshaConfiguration.class)
public class ParikshaConfiguration implements WebMvcConfigurer {

	@Value("${APP_HOME}")
	private String appHome;

	@Autowired
	private OfdsWebSecurityInterceptor securityInterceptor;

	@Bean
    @Profile("!dev")
	ClassLoaderTemplateResolver primaryParikshaThymeleafTemplateResolver() {
		System.out.println("appHome: "+appHome);
		ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
		//secondaryTemplateResolver.setPrefix("META-INF/resources/webjars/pariksha/views/");
//		secondaryTemplateResolver.setPrefix("classpath:/META-INF/resources/webjars/");
		secondaryTemplateResolver.setPrefix("file:"+appHome+"/static/");
		secondaryTemplateResolver.setSuffix(".html");
		secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
		secondaryTemplateResolver.setCharacterEncoding("UTF-8");
		secondaryTemplateResolver.setOrder(1);
		secondaryTemplateResolver.setCheckExistence(true);

		return secondaryTemplateResolver;
	}

    @Bean
    @Profile("dev")
	ClassLoaderTemplateResolver secondaryParikshaThymeleafTemplateResolver() {
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

    @Override
	public void addInterceptors(InterceptorRegistry registry) {

		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(securityInterceptor).addPathPatterns("/pariksha/**");
	}

}
