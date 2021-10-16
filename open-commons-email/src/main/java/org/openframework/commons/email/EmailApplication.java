package org.openframework.commons.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.openframework.commons.constants.StringConstants;
import org.openframework.commons.email.service.DefaultEmailServiceImpl;
import org.openframework.commons.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EmailApplication {

	@Autowired
	private ApplicationContext appContext;

	@Value("${commons.email.thread-pool-executor.size:1}")
	private Integer threadPoolExecutorSize;

	ExecutorService executor;

	@PostConstruct
	public void init() {
		executor = Executors.newFixedThreadPool(threadPoolExecutorSize);
	}

	public void execute(EmailService emailService) {
		executor.execute(emailService);
	}

	/**
	 * This is a sample method to show how async (non-blocking) call can be used to send email.
	 */
	public void sendAsyncEmailForSampleTemplate() {

		EmailService emailService = appContext.getBean(DefaultEmailServiceImpl.class);
		emailService.init(EmailConstants.SAMPLE_EMAIL_TEMPLATE, null, StringConstants.EMPTY_STRING_ARRAY);
		this.execute(emailService);
	}

	/**
	 * This is a sample method to show how synchronous (blocking) call can be used to send email.
	 */
	public void sendSyncEmailForSampleTemplate() {

		EmailService emailService = appContext.getBean(DefaultEmailServiceImpl.class);
		emailService.init(EmailConstants.SAMPLE_EMAIL_TEMPLATE, null, StringConstants.EMPTY_STRING_ARRAY);
		emailService.run();
	}

}
