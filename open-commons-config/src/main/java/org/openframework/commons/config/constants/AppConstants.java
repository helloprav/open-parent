package org.openframework.commons.config.constants;

public class AppConstants {

	public static final String CONFIG_APP_NAME = "gconfig";
	public static final String SLASH_CONFIG_APP = "/"+CONFIG_APP_NAME;
	public static final String SLASH_CONFIG_APP_SLASH = "/"+CONFIG_APP_NAME + "/";

	public static final String CONFIG_APP_PATH = "/"+CONFIG_APP_NAME + "/app";
	public static final String CONFIG_APP_PATH_ASTRIX = CONFIG_APP_PATH + "/*";

	public static final String CONFIG_API_PATH = "/"+CONFIG_APP_NAME + "/api";
	public static final String CONFIG_API_L10N_PATH = CONFIG_API_PATH + "/l10n";

	public static final String CONFIG_DIR_KEY = "configKey";
	public static final String MESSAGE_DIR_KEY = "messageKey";
	public static final String MESSAGE_TYPE_DASHBOARD = "dashboard";
	public static final String MESSAGE_TYPE_ERRORS = "errors";

	public static final String SHARED_PATH = "sharedPath";
	public static final String APPLICATION_CONFIG_DIR = "cmnConfig";
	public static final String APPLICATION_MESSAGE_DIR = "cmnMessage";
	public static final String APPLICATION_LOG_DIR = "logs";

	public static final String GLOBAL_CONFIG = "global-config";

	public static final String APPLICATION_ENV_DEV = "dev";
	public static final String APPLICATION_ENV_PROD = "prod";

    public static final String APP_INITI_FILE = "appInitialization.yml";

	public static final String FORWARD_SLASH = "/";
	public static final String URL_CONTEXT_ROOT = FORWARD_SLASH;

	public static final String SECURITY_SERVICE_APP_URL = "/app";
	public static final String SECURITY_SECURE_MAPPING = "/secure";
	public static final String SECURITY_CONFIG = "security.config";
    public static final String SECURITY_CONFIG_MAPPING = "/app/*";
    // tomcat role
    public static final String TOMCAT_ROLE = "manager-gui";
    public static final String TOMCAT_ROLE_ROLE1 = "role1";
	public static final String SPRING_MVC_SERVLET_URL_PATTERN = "/*";

	public static final String FILE_EXTENSION_PROPERTIES = "properties";
	public static final String FILE_EXTENSION_YML = "yml";
}
