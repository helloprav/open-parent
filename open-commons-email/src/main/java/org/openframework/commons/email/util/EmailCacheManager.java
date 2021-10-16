package org.openframework.commons.email.util;

import javax.xml.bind.JAXBException;

import org.openframework.commons.cache.GlobalCacheApp;
import org.openframework.commons.constants.CommonConstants;
import org.openframework.commons.email.EmailConstants;
import org.openframework.commons.email.vo.EmailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheManager = GlobalCacheApp.caffeineCacheManager, cacheNames = EmailConstants.EMAIL_CACHE)
public class EmailCacheManager {

	private static Logger logger = LoggerFactory.getLogger(EmailCacheManager.class);

	@Value("${commons.email.template.folder:}")
	private String templateFolder;

	public EmailCacheManager() {
		// no arg constructor stub
	}

	static {
		GlobalCacheApp.addCacheName(GlobalCacheApp.caffeineCacheManager, EmailConstants.EMAIL_CACHE);
	}

	@Cacheable(key = "#templateName")
	public EmailVO getEmailVO(String templateName) {
		String templatePath = null;
		EmailVO emailVo = null;
		try {
			if(null != templateFolder && templateFolder.endsWith(CommonConstants.STR_FORWARD_SLASH)) {
				templatePath = templateFolder.concat(templateName);
			} else {
				templatePath = templateName;
			}
			emailVo = (EmailVO) JaxbUtils.loadClassFromJaxb(templatePath, EmailVO.class);
		} catch (JAXBException e) {
			logger.error("Exception caught  while loading template: " + templateName, e);
		}
		return emailVo;
	}
}
