package org.openframework.commons.masterdata.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * The persistent class for the groups database table.
 * 
 */
public class AdminStatusVO {

	private Long id;
	private String name;
	private String status;
	private String description;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-M-yyyy hh:mm:ss", locale = "en-IN", timezone = "UTC")
	private Date createdDate;

	public AdminStatusVO() {
		// no argument constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}