package org.openframework.commons.email.util;

import java.util.UUID;

import javax.inject.Inject;

import org.openframework.commons.email.vo.EmailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

	private static Logger logger = LoggerFactory.getLogger(EmailUtils.class);

	@Inject
	private EmailCacheManager emailCacheManager;

	public static String generatePassword() {
		return UUID.randomUUID().toString();
	}

	public EmailVO getEmailVoFromTemplate(String templateName) {

		EmailVO clone = null;
		EmailVO emailVo = emailCacheManager.getEmailVO(templateName);
		if(null != emailVo) {
			try {
				clone = (EmailVO)emailVo.clone();
			} catch (CloneNotSupportedException e) {
				logger.error("Exception caught  while cloning emailVO for template: " + templateName, e);
			}
		}
		return clone;
	}
}
