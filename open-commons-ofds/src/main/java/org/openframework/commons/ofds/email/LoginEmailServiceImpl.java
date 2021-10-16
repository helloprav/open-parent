package org.openframework.commons.ofds.email;

import org.openframework.commons.email.service.EmailService;
import org.openframework.commons.email.vo.EmailVO;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.utils.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LoginEmailServiceImpl extends EmailService {

	@Override
	public void updateEmailVO(EmailVO emailVO) {

		// cast EmailService's data object into UserVO
		UserVO userVO = (UserVO)getData();
		String messageBody = emailVO.getBody();
		messageBody = StringUtil.replaceToken(messageBody, "@userName@", userVO.getUsername());
		messageBody = StringUtil.replaceToken(messageBody, "@firstName@", userVO.getFirstName());
		if(null != userVO.getPassword()) {
			messageBody = StringUtil.replaceToken(emailVO.getBody(), "@password@", userVO.getPassword().toString());
		}
		emailVO.setBody(messageBody);
	}

}
