/**
 * 
 */
package org.openframework.commons.domain.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Java Developer
 *
 */
public class ApplicationRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5076424288173160260L;

	private List<String> validationErrors;

	public ApplicationRuntimeException() {
		// no args constructor
	}

	public ApplicationRuntimeException(String errorMessage) {

		addValidationErrors(errorMessage);
	}

	public ApplicationRuntimeException(List<String> validationErrors) {
		this.validationErrors = validationErrors;
	}
	public List<String> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List<String> validationError) {
		this.validationErrors = validationError;
	}

	public ApplicationRuntimeException addValidationErrors(String validationError) {
		if(null == this.validationErrors) {
			this.validationErrors = new ArrayList<String>();
		}
		this.validationErrors.add(validationError);
		return this;
	}

}
