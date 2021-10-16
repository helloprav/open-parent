package org.openframework.commons.config.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Credentials {

	private static Credentials instance;
	private static int counter;
	private Properties credProperties;

	private Credentials() {
		System.out.println("Constructor called: " + counter++);
	}

	public static Credentials getInstance() {
		if (null == instance) {
			synchronized (Credentials.class) {
				if (null == instance) {
					instance = new Credentials();
					instance.read();
				}
			}
		}
		return instance;
	}

	/**
	 * @return the credProperties
	 */
	public Properties getCredProperties() {
		if (null == credProperties) {
			this.read();
		}
		return credProperties;
	}

	private void read() {
		final Properties properties = new Properties();
		try (final InputStream stream = Credentials.class.getClassLoader().getResourceAsStream("cred.properties")) {
			if (null == stream) {

			} else {
				properties.load(stream);
				System.out.println("Properties loaded: " + properties);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		credProperties = properties;
	}

}
