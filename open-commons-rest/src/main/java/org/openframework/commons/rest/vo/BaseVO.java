package org.openframework.commons.rest.vo;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

public class BaseVO {

	private Boolean isValid;
	private String createdBy;
	// @JsonDeserialize(using = CustomJsonDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-M-yyyy hh:mm:ss", locale = "en-IN", timezone = "UTC")
	private Date createdDate;
	private String modifiedBy;
	// @Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-M-yyyy hh:mm:ss", locale = "en-IN", timezone = "UTC")
	private Date modifiedDate;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

}
