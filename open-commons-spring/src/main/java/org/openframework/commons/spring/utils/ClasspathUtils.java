package org.openframework.commons.spring.utils;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ClasspathUtils {

	private static final Logger logger = LoggerFactory.getLogger(ClasspathUtils.class);

	public static void main(String[] args) throws IOException {

		System.out.println(getPathFromClasspathFolder("file.txt"));
		System.out.println(getPathFromClasspathFolder("config"));
	}

	public static String getPathFromClasspathFolder(String classpathFolder) {

		String filePath = null;
		Resource resource = new ClassPathResource(classpathFolder);
		logger.debug("resource: {}", resource);
		try {
			filePath = resource.getFile().getPath();
		} catch (IOException e) {
			logger.error("Error while getting the path for: {}", classpathFolder);
		}
		return filePath;
	}

	public static File[] getFilesFromClasspath(String classpathFolder) {

		String path = getPathFromClasspathFolder(classpathFolder);
		if (null == path) {
			return null;
		}
		return new File(path).listFiles();
	}
}
