package org.openframework.commons.rest.beans;

import org.openframework.commons.spring.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created by francesco on 2/11/14.
 */
// @JsonSerialize(include=Inclusion.NON_NULL) // Jackson 1.9 or lower
@JsonInclude(Include.NON_NULL) // Jackson 2 or higher
//@XmlRootElement(name="response")
public class ResponseBean<T> extends ErrorBean {

	private Long responseTime;
	private int statusCode;
	private String successMessage;
	private String developerMessage;
	private T data;
	private Pagination pagination;

	public ResponseBean() {
		// do nothing
	}

	public ResponseBean(int statusCode) {
		this.statusCode = statusCode;
	}

	public ResponseBean(String successMessage) {
		this.successMessage = successMessage;
	}

	public ResponseBean(int statusCode, String message) {
		this.statusCode = statusCode;
		this.successMessage = message;
	}

	public ResponseBean(int statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.successMessage = message;
		this.data = data;
	}

	public ResponseBean(int statusCode, String message, ErrorBean errorBean) {
		super(errorBean.getErrorCode(), errorBean.getErrorList());
		this.statusCode = statusCode;
		this.successMessage = message;
	}

	public Long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName()+"[responseTime="+getResponseTime()+", statusCode="+getStatusCode()+", data="+data+"]";
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
}
