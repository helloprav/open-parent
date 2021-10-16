package org.openframework.commons.rest.advice;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

import javax.inject.Inject;
import javax.management.ServiceNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.openframework.commons.config.service.as.MessageResourceAS;
import org.openframework.commons.domain.exceptions.ApplicationRuntimeException;
import org.openframework.commons.domain.exceptions.ApplicationValidationException;
import org.openframework.commons.domain.exceptions.AuthenticationException;
import org.openframework.commons.domain.exceptions.EntityConflictsException;
import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.domain.exceptions.ErrorCode;
import org.openframework.commons.domain.exceptions.PermissionDeniedException;
import org.openframework.commons.rest.Constants;
import org.openframework.commons.rest.beans.ErrorBean;
import org.openframework.commons.rest.beans.ResponseBean;
import org.openframework.commons.rest.exception.KeywordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * For more details on this class, please read:
 * http://www.concretepage.com/spring/spring-mvc/spring-mvc-exception-handling-with-exceptionhandler-responseStatus-handlerexceptionresolver-example-global-exception
 * http://www.concretepage.com/spring/spring-mvc/spring-mvc-controlleradvice-annotation-example
 * http://www.concretepage.com/spring/spring-mvc/spring-mvc-validator-with-initbinder-webdatabinder-registercustomeditor-example
 * 
 * This class should return only ErrorBean populated with necessary info, which
 * will be converted into {@link ResponseBean} in {@link RestResponseBodyAdvice}
 * 
 * @author pmis30
 *
 */
@ControllerAdvice(annotations = RestController.class)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String SIZE = "Size";
	private static final String STR_DOT = ".";

	private Logger log = LoggerFactory.getLogger(getClass());

	// @Inject
	// @Autowired
	// private MessageSource messageSource;

	@Inject
	private Optional<MessageResourceAS> messageResourceAS;

	@InitBinder
	public void dataBindingGlobal(WebDataBinder binder) {
		log.debug("RestResponseEntityExceptionHandler.dataBindingGlobal()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "dob", new CustomDateEditor(dateFormat, true));
	}

	/**
	 * The below @ModelAttribute annotated method is not required in case of REST
	 * service. It may be useful in case there is a view/jsp associated.
	 * 
	 * 2q 1 * @param model
	 */
	@ModelAttribute
	public void globalAttributes(Model model) {
		model.addAttribute("msg", "Welcome to My World!");
	}

	/**
	 * Probably this method is not used as handleMethodArgumentNotValid() is taking
	 * care of validation error.
	 * 
	 * @param ex
	 * @param request
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(value = { ApplicationRuntimeException.class, KeywordNotFoundException.class, EntityConflictsException.class })
	protected ErrorBean handleApplicationRuntimeException(ApplicationRuntimeException ex, WebRequest request) {

		HttpServletResponse response = ((ServletWebRequest) request).getResponse();
		response.setStatus(HttpStatus.BAD_REQUEST.value());

		// ideally ValidationBean should be returned from controller after
		// validation
		if (ex instanceof ApplicationRuntimeException) {
			return new ErrorBean(ErrorCode.VALIDATION_ERROR,
					resolveErrorMessage(ex.getValidationErrors()));
		} else if (ex instanceof ApplicationValidationException) {
			return new ErrorBean(ErrorCode.VALIDATION_ERROR,
					resolveErrorMessage(((ApplicationValidationException) ex).getValidationErrors()));
		} else {
			return new ErrorBean(ErrorCode.VALIDATION_ERROR);
		}
	}

	/**
	 * Creates list of errors (localized if locale properties found) from the single
	 * errorMessage. If no locale properties found, the errorMessage itself is
	 * returned by wraping in a list.
	 * 
	 * @param errorMessage
	 * @return List<String> of errorMessage
	 */
	private List<String> resolveErrorMessage(String errorMessage) {

		ArrayList<String> errorList = new ArrayList<>();

		if (messageResourceAS.isPresent()) {
			Locale currentLocale = LocaleContextHolder.getLocale();
			String localizedErrorMessage = messageResourceAS.get().getErrorMessageValue(currentLocale.getLanguage(), errorMessage);
			if(null != localizedErrorMessage) {
				errorMessage = localizedErrorMessage;
			}
		}
		errorList.add(errorMessage);
		return errorList;
	}

	private List<String> resolveErrorMessage(List<String> errorCodeList) {

		if (messageResourceAS.isPresent()) {
			return resolveLocalizedErrorMessage(errorCodeList);
		} else {

			/**
			 * Build the errorMessage based on the VO's annotation message
			 * [@NotNull(message="{NotNull.name}")] i.e. "NotNull.name".
			 */
			return errorCodeList;
		}
	}

	private List<String> resolveLocalizedErrorMessage(List<String> errorCodeList) {

		List<String> errorList = null;
		Locale currentLocale = LocaleContextHolder.getLocale();

		if (messageResourceAS.isPresent()) {

			errorList = new ArrayList<>();
			for(String errorCode : errorCodeList) {
				String errorMessage = messageResourceAS.get().getErrorMessageValue(currentLocale.getLanguage(), errorCode);
				if(null == errorMessage) {
					errorMessage = errorCode;
				}
				errorList.add(errorMessage);
			}
		}
		return errorList;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = { ServiceNotFoundException.class })
	protected ErrorBean handleServiceNotFoundException(Exception ex, WebRequest request) {

		log.error("Unable to find service 1", ex);
		return new ErrorBean(ErrorCode.SERVICE_NOT_FOUND);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = { EntityNotFoundException.class })
	protected ErrorBean handleEntityNotFoundException(Exception ex, WebRequest request) {

		// ideally ValidationBean should be returned from controller after
		// validation
		return new ErrorBean(ErrorCode.ENTITY_NOT_FOUND,
				resolveErrorMessage(((ApplicationRuntimeException) ex).getValidationErrors()));
	}

	/**
	 * This method handles the client error: like any input data validation
	 * http://www.baeldung.com/global-error-handler-in-a-spring-rest-api
	 * <p>
	 * {@inheritDoc}
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			String localizedErrorMessage = resolveErrorMessage(error);
			errors.add(error.getField() + ": " + localizedErrorMessage);
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		ErrorBean apiError = new ErrorBean(ErrorCode.VALIDATION_ERROR, errors);
		return new ResponseEntity<>(apiError, headers, status);
	}

	/**
	 * This is able to resolve the localized error message only from
	 * ValidationMessage.properties files. This is not able to resolve the localized
	 * error message from other resource bundle even if it is configured as a bean
	 * through JavaConfig or Xml Config.
	 * 
	 * @param fieldError
	 * @return
	 */
	private String resolveErrorMessage(FieldError fieldError) {

		if (messageResourceAS.isPresent()) {
			return resolveLocalizedErrorMessage(fieldError);
		} else {

			/**
			 * Build the errorMessage based on the VO's annotation message
			 * [@NotNull(message="{NotNull.name}")] i.e. "NotNull.name".
			 */
			return fieldError.getDefaultMessage();
		}
	}

	private String resolveLocalizedErrorMessage(FieldError fieldError) {

		Locale currentLocale = LocaleContextHolder.getLocale();
		String errorMessage = null;

		Properties localeErrorProperties = null;
		if (messageResourceAS.isPresent()) {
			localeErrorProperties = messageResourceAS.get().getMessageResourceMap().get(Constants.MESSAGE_TYPE_ERRORS)
					.getPropertiesMap().get(currentLocale.getLanguage());

			if(SIZE.equals(fieldError.getCode())) {
				errorMessage = getErrorMessageForSize(fieldError, localeErrorProperties);
				if(null == errorMessage) {
					
				}
			}
			/*
			 * Build the errorMessage based on the VO's annotation message
			 * [@NotNull(message="{NotNull.name}")] i.e. "NotNull.name".
			 */
			if (null == errorMessage) {
				errorMessage = localeErrorProperties.getProperty(fieldError.getDefaultMessage());
			}
			if (null == errorMessage) {
				// If its's value in errorMessage Property file is null, then
				errorMessage = localeErrorProperties.getProperty(fieldError.getCode() + "." + fieldError.getField());
			}
			if (null == errorMessage) {
				// If its's value in errorMessage Property file is null, then
				errorMessage = localeErrorProperties.getProperty(fieldError.getCode());
			}
			if (null == errorMessage && !MessageResourceAS.DEFAULT_LOCALE.equals(currentLocale.getLanguage())) {
				errorMessage = messageResourceAS.get().getErrorMessageValue(currentLocale.getLanguage(), fieldError.getCode());
			}
			if(null == errorMessage) {
				// If its's value in errorMessage Property file is null, then
				errorMessage = fieldError.getDefaultMessage();
			}
		}
		return errorMessage;
	}

	private String getErrorMessageForSize(FieldError fieldError, Properties localeErrorProperties) {

		String errorMessage = localeErrorProperties.getProperty(fieldError.getCode());
		if(null != errorMessage) {
			errorMessage = MessageFormat.format(errorMessage, fieldError.getArguments()[2],
					fieldError.getArguments()[1]);
		}
		return errorMessage;
	}

	@SuppressWarnings("unused")
	private Object getFieldName(FieldError fieldError) {

		return fieldError.getObjectName().concat(STR_DOT).concat(fieldError.getField());
	}

	/**
	 * This method here is to log any such error for log analysis.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.warn(ex.getMessage(), ex);
		return super.handleExceptionInternal(ex, body, headers, status, request);

	}

	/**
	 * This method here is to log any such error for log analysis.
	 * <p>
	 * {@inheritDoc}
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value = { AuthenticationException.class })
	protected ErrorBean handleAuthenticationException(AuthenticationException ex) {
		//log.info(ex.getMessage(), ex);
		return new ErrorBean(ErrorCode.AUTHENTICATION_ERROR, resolveErrorMessage(ex.getValidationErrors()));
	}

	/**
	 * This method here is to log any such error for log analysis.
	 * <p>
	 * {@inheritDoc}
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(value = { PermissionDeniedException.class })
	protected ErrorBean handlePermissionDeniedException(PermissionDeniedException ex) {
		//log.warn(ex.getMessage(), ex);
		return new ErrorBean(ErrorCode.ACCESS_DENIED);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
	protected ErrorBean handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		log.warn(ex.getMessage(), ex);
		return new ErrorBean(ErrorCode.VALIDATION_ERROR, resolveErrorMessage(ex.getCause().getMessage()));
	}

}