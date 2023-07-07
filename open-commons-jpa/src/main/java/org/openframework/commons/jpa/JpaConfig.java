package org.openframework.commons.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@EnableJpaRepositories(basePackages = "org.openframework.commons.ofds")
@EntityScan(basePackageClasses = JpaConfig.class)
public class JpaConfig {

}
