package org.openframework.commons.ofds.controller.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.openframework.commons.ofds.constant.OfdsConstants;
import org.openframework.commons.ofds.props.MasterDataProps;
import org.openframework.commons.ofds.props.OfdsSecurityProps;
import org.openframework.commons.rest.interceptor.AbstractSecurityInterceptor;
import org.openframework.commons.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;

public abstract class OfdsBaseHandlerInterceptor extends AbstractSecurityInterceptor {

	@Autowired
	private OfdsSecurityProps ofdsSecurityProps;

	@Autowired
	private MasterDataProps masterDataProps;

	public OfdsSecurityProps getOfdsSecurityProps() {
		return ofdsSecurityProps;
	}

	public MasterDataProps getMasterDataProps() {
		return masterDataProps;
	}

	protected boolean isUnprotectedRequest(HttpServletRequest request, String path) {

		logger.debug("Request Path: {}", path);
		CookieUtils.printRequestDetails(request);
		for(String url: ofdsSecurityProps.getUnprotectedUrls()) {
			if(path.startsWith(url)) {
				return true;
			}
		}
		for(String urlPattern: ofdsSecurityProps.getUnprotectedUris()) {
			String url = StringUtils.removeEnd(urlPattern, OfdsConstants.SLASH_ASTRIX);
			if(path.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

}
