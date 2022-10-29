package org.openframework.commons.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfiguration {

	@Value("${commons.swagger.title:SpringDoc/OpenAPI's REST API Collection}")
	private String openApiTitle;

	@Value("${commons.swagger.description:REST API Collection for all the modules}")
	private String openApiDesc;

	@Value("${commons.swagger.version:}")
	private String version;

	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(openApiTitle)
                .description(openApiDesc)
                .version(version)
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License()
                .name("GPL Licence")
                .url("https://docs.github.com/en/site-policy/github-terms/github-terms-of-service");
    }

    private Contact apiContact() {
        return new Contact()
                .name("praveen kumar mishra")
                .email("pranawork2017@gmail.com")
                .url("https://github.com/helloprav");
    }
}