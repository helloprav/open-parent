package org.openframework.commons.rest.beans;

import java.util.List;

/**
 * Created by francesco on 2/11/14.
 */
public class ValidationBean {

	private List<String> errorList;

	public ValidationBean() {
		// no arg constructor, actually not required
	}

	public ValidationBean(List<String> validationErrors) {
		this.errorList = validationErrors;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

}
