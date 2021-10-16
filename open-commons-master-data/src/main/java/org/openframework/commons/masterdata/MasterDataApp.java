package org.openframework.commons.masterdata;

import org.openframework.commons.masterdata.vo.MasterData;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableConfigurationProperties({ MasterData.class })
@EnableJpaRepositories(basePackages = "org.openframework.commons.masterdata")
@EntityScan(basePackageClasses = MasterDataApp.class)
public class MasterDataApp {

}
