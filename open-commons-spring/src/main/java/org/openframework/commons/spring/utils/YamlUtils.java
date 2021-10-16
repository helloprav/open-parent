package org.openframework.commons.spring.utils;

import java.io.File;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;

public class YamlUtils {

	private YamlUtils() {
		throw new IllegalStateException("Utility class");
	}

    public static Properties loadYamlProperty(String fileName) {

    	final File file = new File(fileName);
        return loadYamlFromFile(file);
    }

    public static Properties loadYamlFromFile(File file) {

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new FileSystemResource(file));
        return yaml.getObject();
    }
}
