/**
 * 
 */
package org.openframework.commons.domain.exceptions;

/**
 * @author Java Developer
 *
 */
public class AuthenticationException extends ApplicationRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7241355620740425238L;

	public AuthenticationException() {	}

	public AuthenticationException(String message) {

		addValidationErrors(message);
	}

}
