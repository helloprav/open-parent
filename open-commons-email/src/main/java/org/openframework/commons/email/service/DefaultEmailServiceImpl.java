package org.openframework.commons.email.service;

import org.openframework.commons.email.vo.EmailVO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DefaultEmailServiceImpl extends EmailService {

	@Override
	public void updateEmailVO(EmailVO emailVO) {
		// empty implementation
	}

}
