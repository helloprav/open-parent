package org.openframework.commons.spring.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 	// https://www.youtube.com/watch?v=UjYv7HfTrlc
 * 
 * @author Java Developer
 *
 */
@Component
public class LoggingBean implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Environment env;

	@Autowired
	protected ApplicationContext ctx;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("=================================================================");
		try {
			logSpringBeans();
			logEnvVariables();
			logActiveSpringProfiles();
			logReleventProperties();
		} catch (Exception e) {
			logger.error("LoggingBean failed: {}", e.getMessage());
		}
		logger.info("=================================================================");
	}

	private void logSpringBeans() {
		System.out.println("\n\n\nPrinting all bean names::::::::"+ ctx);
		SpringUtils.getSpringBeans();
	}

	private void logEnvVariables() {

		final String envTargetValue = getValueOfProperty(env, "envTarget", "local", null);
		logger.info("{} = {}", "envTarget", envTargetValue);
	}

	private void logActiveSpringProfiles() {

		String key = "spring.profiles.active";
		String value = getValueOfProperty(env, key, "local", null);
		logger.info("{} = {}", key, value);
	}

	private void logReleventProperties() {

		String key = "dbc.url";
		String value = getValueOfProperty(env, key, null, null);
		logger.info("{} = {}", key, value);
	}

	private final String getValueOfProperty(final Environment environment, final String propertyKey,
			final String propertyDefaultValue, final List<String> acceptablePropertyValues) {
		String propValue = environment.getProperty(propertyKey);
		if (null == propValue) {
			propValue = propertyDefaultValue;
			logger.warn("The {} doesn't have an explicit value; default value is: {}", propertyKey,
					propertyDefaultValue);
		}
		if(null != acceptablePropertyValues && !acceptablePropertyValues.contains(propValue)) {
			logger.warn("The property = {} has an invalid value = {} ", propertyKey, propValue);
		}
		if(null == propValue) {
			logger.warn("The property = {} has NULL value");
		}
		return propValue;
	}
}
