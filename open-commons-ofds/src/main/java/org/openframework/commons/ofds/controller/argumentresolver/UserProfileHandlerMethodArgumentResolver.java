package org.openframework.commons.ofds.controller.argumentresolver;

import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.ofds.controller.interceptor.OfdsSecurityInterceptor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserProfileHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver  {

	public UserProfileHandlerMethodArgumentResolver() {
		System.out.println("UserProfileHandlerMethodArgumentResolver.constructor");
	}

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().equals(UserVO.class);
	}

	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest nativeWebRequest,
			WebDataBinderFactory webDataBinderFactory) throws Exception {

		return OfdsSecurityInterceptor.getUserProfile();
	}

}
