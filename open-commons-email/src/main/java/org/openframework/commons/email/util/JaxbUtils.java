package org.openframework.commons.email.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class JaxbUtils {

	private static Logger logger = LoggerFactory.getLogger(JaxbUtils.class);

	@SuppressWarnings("rawtypes")
	public static Object loadClassFromJaxb(String xmlTemplateName, Class clazz) throws JAXBException {

		InputStream in = JaxbUtils.class.getClassLoader().getResourceAsStream(xmlTemplateName);
		if (null == in) {
			try {
				InputStream resource = new ClassPathResource(xmlTemplateName).getInputStream();
				in = resource;
			} catch (IOException ex) {
				logger.error("Exception caught while loading template: "+ xmlTemplateName, ex);
				return null;
			}
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return jaxbUnmarshaller.unmarshal(in);
	}
}
