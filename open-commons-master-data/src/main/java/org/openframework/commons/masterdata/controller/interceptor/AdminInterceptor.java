package org.openframework.commons.masterdata.controller.interceptor;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openframework.commons.enums.Status;
import org.openframework.commons.masterdata.service.bo.AdminService;
import org.openframework.commons.masterdata.vo.AdminStatusVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter("/api/*")
public class AdminInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AdminInterceptor.class);
	private static boolean allowRegularRequest = false;

	@Inject
	AdminService adminService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.debug("Entered AdminInterceptor: {}", request.getRequestURL());
		if(allowRegularRequest) {
			return true;
		}
		boolean allowRegularRequestFlag = true;
		List<AdminStatusVO> statusList = adminService.getStatus();
		ListIterator<AdminStatusVO> listIterator = statusList.listIterator();

		while (listIterator.hasNext()) {
			AdminStatusVO adminStatusVO = listIterator.next();
			if(!Status.completed.toString().equals(adminStatusVO.getStatus())) {
				allowRegularRequestFlag = false;
			}
		}
		if(allowRegularRequestFlag) {
			AdminInterceptor.updateAllowRequest(true);
			return true;
		} else {
			response.sendRedirect("/ofds/admin/error");
		}
		return true;
	}

	private static void updateAllowRequest(boolean b) {
		allowRegularRequest = true;
	}

}
