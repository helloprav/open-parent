package org.openframework.commons.jpa;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class AbstractTransactionManagementConfig {

	static {
		System.out.println("AbstractTransactionManagementConfig.static{} ");
	}

	@Autowired
	private Environment env;

	@Bean
	public PlatformTransactionManager transactionManager() {
		System.out.println("AbstractTransactionManagementConfig.transactionManager");
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

		System.out.println("AbstractTransactionManagementConfig.entityManagerFactory");
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPersistenceUnitName("hibernatePersistenceUnit");
		//em.setPackagesToScan("org.school.userandsecurity.service.entity");
		em.setPackagesToScan(getEntityPackagesToScan());
		em.setJpaVendorAdapter(getJpaVendorAdapter());
		em.setJpaProperties(getJpaProperties());
		return em;
	}

	public abstract String getEntityPackagesToScan();

	/**
	 * This is not a @Bean, but a local method to separate the getJpaProperties
	 * logic
	 * 
	 * @return
	 */
	private Properties getJpaProperties() {

		System.out.println("AbstractTransactionManagementConfig.getJpaProperties");
		Properties prop = new Properties();
		//prop.setProperty("hibernate.hbm2ddl.auto", "validate");
		prop.setProperty("hibernate.hbm2ddl.auto", getHbm2ddlAuto());
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		//prop.setProperty("hibernate.cache.use_query_cache", "true");
		//prop.setProperty("hibernate.cache.use_second_level_cache", "true");
		//prop.setProperty("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
		//prop.setProperty("hibernate.cache.provider_configuration_file_resource_path", "classpath:config/ehcache.xml");
		return prop;
	}

	public abstract String getHbm2ddlAuto();

	/**
	 * 
	 * @return
	 */
	@Bean
	public HibernateJpaVendorAdapter getJpaVendorAdapter() {

		System.out.println("AbstractTransactionManagementConfig.getJpaVendorAdapter");
		HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
		vendor.setShowSql(true);
		vendor.setGenerateDdl(true);
		vendor.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		return vendor;
	}

	@Bean
	public DataSource dataSource() {

		System.out.println("AbstractTransactionManagementConfig.dataSource");
		ComboPooledDataSource ds = new ComboPooledDataSource();
		try {
			ds.setDriverClass(env.getRequiredProperty("jdbc.driverClassName"));
			ds.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
			ds.setUser(env.getRequiredProperty("jdbc.username"));
			ds.setPassword(env.getRequiredProperty("jdbc.password"));

			// C3P0 specific DataSource properties
			ds.setInitialPoolSize(Integer.parseInt(env.getRequiredProperty("c3p0.initialPoolSize")));
			ds.setMinPoolSize(Integer.parseInt(env.getRequiredProperty("c3p0.minPoolSize")));
			ds.setMaxPoolSize(Integer.parseInt(env.getRequiredProperty("c3p0.maxPoolSize")));
			ds.setAcquireIncrement(Integer.parseInt(env.getRequiredProperty("c3p0.acquireIncrement")));
			ds.setIdleConnectionTestPeriod(Integer.parseInt(env.getRequiredProperty("c3p0.idle_test_period")));
			return ds;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
