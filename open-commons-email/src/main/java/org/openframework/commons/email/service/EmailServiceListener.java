package org.openframework.commons.email.service;

import java.util.Map;

import org.openframework.commons.email.util.EmailUtils;
import org.openframework.commons.email.vo.EmailEvent;
import org.openframework.commons.email.vo.EmailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceListener implements Runnable {

	/** Logger that is available to subclasses in same package*/
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private String templateName;

	private String toAddress[];

	private Object data;

	private boolean initialized;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private JavaMailSender mailSender;

	public EmailServiceListener() {
		// no arg constructor
	}

	public void init(String templateName, Object data, String... toAddress) {
		this.templateName = templateName;
		this.data = data;
		this.toAddress = toAddress;
		this.initialized = true;
	}

	/*
	 * @EventListener ReturnedEvent handleUserRemovedEvent(UserRemovedEvent event) {
	 * // handle UserRemovedEvent ... return new ReturnedEvent(); }
	 */

	@Async
	@EventListener
	void handleSendEmail(Map<String, String> emailMap) {
		// handle ReturnedEvent ...
		System.out.println("sending email: "+emailMap);
	}

	@Async
	@EventListener
	void handleAsyncEvent(String event) {
		// handle event
		System.out.println("Event is caught: "+event);
	}

	@Async
	@EventListener
	void handleAsyncEvent(EmailEvent event) {
		// handle event
		System.out.println("Event.name is caught: "+event.getName());
		System.out.println("Event.map is caught: "+event.getMap());
	}

	@Override
	public void run() {

		if(!initialized) {
			logger.error("EmailService is not yet initialized.");
		}
		EmailVO emailVO = emailUtils.getEmailVoFromTemplate(templateName);
		if(null == emailVO) {
			logger.error("EmailVO is NULL for the template: {}", templateName);
		} else if(emailVO.isEnabled()) {
			updateEmailVO(emailVO);
			emailVO.addTo(toAddress);
			sendSimpleMail(emailVO);
		} else {
			logger.debug("Email template [{}] is disabled", templateName);
		}
	}

	public void sendSimpleMail(EmailVO emailVO) {

		if(null == emailVO || null == emailVO.getTo() || emailVO.getTo().isEmpty()) {
			logger.error("To Address is NULL/Empty for EmailVO: {}", emailVO);
			return;
		}
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailVO.getTo().toArray(new String[emailVO.getTo().size()]));
		message.setSubject(emailVO.getSubject());
		message.setText(emailVO.getBody());
		message.setFrom(emailVO.getFromAddress());
		mailSender.send(message);
		System.out.println("EMAIL IS SENT :)");
	}

	public void updateEmailVO(EmailVO emailVO) {};

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String[] getToAddress() {
		return toAddress;
	}

	public void setToAddress(String... toAddress) {
		this.toAddress = toAddress;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
