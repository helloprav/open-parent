package org.openframework.commons.domain.exceptions;

import java.util.List;

/**
 * Created by francesco on 2/11/14.
 */
public class ApplicationValidationException extends ApplicationRuntimeException {

	/**
	* 
	*/
	private static final long serialVersionUID = 277697923635048585L;

	public ApplicationValidationException() {	}

	public ApplicationValidationException(String exceptionMessage) {

		addValidationErrors(exceptionMessage);
	}

	public ApplicationValidationException(List<String> validationErrors) {
		super(validationErrors);
	}

}
