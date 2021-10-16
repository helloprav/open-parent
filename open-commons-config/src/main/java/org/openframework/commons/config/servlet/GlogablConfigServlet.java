package org.openframework.commons.config.servlet;

import static org.openframework.commons.config.constants.AppConstants.SLASH_CONFIG_APP;
import static org.openframework.commons.config.constants.AppConstants.SLASH_CONFIG_APP_SLASH;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class GlogablConfigServlet
 */
@WebServlet(urlPatterns = { SLASH_CONFIG_APP, SLASH_CONFIG_APP_SLASH }, loadOnStartup = 1)
public class GlogablConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init() throws ServletException {
		// empty implementation
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.sendRedirect(request.getRequestURL() + "/app");
	}

}
