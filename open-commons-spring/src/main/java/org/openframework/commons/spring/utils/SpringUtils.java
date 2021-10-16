package org.openframework.commons.spring.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

//@Component
public class SpringUtils {

	private SpringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Map<String, String> getSpringBeans() {

		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		String[] beanNames = appContext.getBeanDefinitionNames();
		Arrays.parallelSort(beanNames);

		Map<String, String> beansMap = new TreeMap<>();
		for (String beanName : beanNames) {
			beansMap.put(beanName, appContext.getBean(beanName).getClass().getName());
		}
		return beansMap;
	}

	public static boolean isActiveProfile(Environment environment, String profileName) {

		String[] envs = environment.getActiveProfiles();
		for(String env: envs) {
			if(profileName.equals(env)) {
				return true;
			}
		}
		return false;
	}

	public static void printOtherDetails() {

		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		ConfigurableEnvironment env = (ConfigurableEnvironment) appContext.getEnvironment();
		printSources(env);
		System.out.println("---- System properties -----");
		printMap(env.getSystemProperties());
		System.out.println("---- System Env properties -----");
		printMap(env.getSystemEnvironment());
	}

	public static void printSources(ConfigurableEnvironment env) {
		System.out.println("========= property sources =============\n\n");
		for (PropertySource<?> propertySource : env.getPropertySources()) {
			System.out.println("-------------------------------------------");
			System.out.println("name =  " + propertySource.getName() + "\nsource = "
					+ propertySource.getSource().getClass() + "\n");
			if(propertySource.getName().startsWith("applicationConfig")) {

				printPropertySource(propertySource);
			}
		}
	}

	public static void printPropertySource(PropertySource<?> propertySource) {

		System.out.println(propertySource.getSource());
		System.out.println(propertySource.getSource().getClass());
	}

	public static void printMap(Map<?, ?> map) {
		map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));

	}
}
