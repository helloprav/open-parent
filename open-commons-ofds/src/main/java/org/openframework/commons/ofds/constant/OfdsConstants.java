package org.openframework.commons.ofds.constant;

public class OfdsConstants {

	private OfdsConstants() {
		throw new IllegalStateException("Constants class");
	}

	public static final String USERVO = "USERVO";
	public static final String LOGIN_COOKIE_LIST = "LOGIN_COOKIE_LIST";

	public static final String ENV_DEV = "dev";
	public static final String ENV_PROD = "prod";
	public static final String ENV_TEST = "test";

	public static final String API_BASE_PATH = "/api";
	public static final String SLASH_ASTRIX = "/*";

	// config file names, this is apart from the dashboard and error message files
	public static final String CONFIG_APP = "appinitialization";
	public static final String CONFIG_JDBC = "jdbc";
}
