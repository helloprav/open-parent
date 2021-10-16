package org.openframework.commons.config.servlet;

import static org.openframework.commons.config.constants.AppConstants.CONFIG_APP_PATH;
import static org.openframework.commons.config.constants.AppConstants.CONFIG_APP_PATH_ASTRIX;
import static org.openframework.commons.config.constants.AppConstants.SECURITY_SECURE_MAPPING;
import static org.openframework.commons.config.constants.AppConstants.TOMCAT_ROLE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class SecurityServlet
 */
@WebServlet(urlPatterns = { CONFIG_APP_PATH, CONFIG_APP_PATH_ASTRIX }, loadOnStartup = 1)
@ServletSecurity(@HttpConstraint(rolesAllowed = {
		TOMCAT_ROLE }, transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class GlobalConfigSecurityServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";

	@Override
	public void init() throws ServletException {

		//getServletContext().setAttribute("configAppName", CONFIG_APP_NAME);
		System.out.println("GlobalConfigSecurityServlet.init()");
		super.init();
		System.out.println("GlobalConfigSecurityServlet.init(2)");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.warn("App Settings page accessd by User");
		boolean isAuthenticated = doAuthenticate(request);
		if (!isAuthenticated) {
			response.setHeader("WWW-Authenticate", "Basic realm=\"Config App Authentication\"");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
			return;
		}
		String appUrl = request.getContextPath().concat(CONFIG_APP_PATH);
		RequestDispatcher rd = null;

		// TODO /api/* mapping for the dispather servlet has to be taken care of here
		rd = request.getRequestDispatcher(request.getRequestURI().replaceAll(appUrl, SECURITY_SECURE_MAPPING));
		/*
		 * if (appUrl.equals(request.getRequestURI())) { rd =
		 * request.getRequestDispatcher("/api/secure"); } else { rd =
		 * request.getRequestDispatcher(request.getRequestURI().replaceAll(appUrl,
		 * AppConstants.SECURITY_SECURE_MAPPING)); }
		 */
		request.setAttribute(SECURITY_SECURE_MAPPING, true);
		rd.forward(request, response);
	}

	private boolean doAuthenticate(HttpServletRequest request) {
		boolean isAuthenticated = false;
		boolean isUserInRole = request.isUserInRole(TOMCAT_ROLE);
		if (isUserInRole) {
			isAuthenticated = true;
		} else {
			StringBuilder user = new StringBuilder();
			StringBuilder password = new StringBuilder();

			boolean isBasicAuth = isBasicAuth(request, user, password);
			if (isBasicAuth) {
				isAuthenticated = validateCredentials(user.toString(), password.toString());
			}
		}
		return isAuthenticated;
	}

	private boolean validateCredentials(String user, String password) {
		return hasProperty(user, password);
	}

	public boolean hasProperty(String key, String value) {
		Credentials cred = Credentials.getInstance();
		Properties prop = cred.getCredProperties();
		if(prop.containsKey(key)) {
			String propValue = prop.getProperty(key);
			return value.equals(propValue);
		}
		return false;
	}

	private boolean isBasicAuth(HttpServletRequest request, StringBuilder userName, StringBuilder password) {
		boolean isBasicAuth = false;
		String basicCredentials = getAuthHeader(request);
		if (StringUtils.isNotBlank(basicCredentials)) {
			isBasicAuth = basicCredentials.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase());
		}
		final String basicAuthEncoding = "UTF-8";
		if (isBasicAuth) {
			Base64.Decoder decoder = Base64.getDecoder();
			String strNameAndPwd;
			try {
				strNameAndPwd = new String(
						decoder.decode(basicCredentials.substring(6).trim().getBytes(basicAuthEncoding)),
						Charset.forName(basicAuthEncoding));
				int nIndex = strNameAndPwd.indexOf(":");
				userName.append(strNameAndPwd.substring(0, nIndex));
				password.append(strNameAndPwd.substring(nIndex + 1));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				isBasicAuth = false;
			}
		}
		return isBasicAuth;
	}

	private String getAuthHeader(HttpServletRequest request) {

		String authHeader = request.getHeader(AUTHORIZATION_PROPERTY);
		if (StringUtils.isBlank(authHeader)) {
			authHeader = request.getHeader(AUTHORIZATION_PROPERTY.toLowerCase());
		}
		return authHeader;
	}

}
