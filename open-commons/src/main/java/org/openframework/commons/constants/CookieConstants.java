package org.openframework.commons.constants;

public class CookieConstants {

	private CookieConstants() {
		throw new IllegalStateException("Constant class");
	}

	// TODO make it configurable through property file
	public static final int COOKIE_EXPIRY_TIME = 150;

	public static final String COOKIE_PATH = "/";

}
