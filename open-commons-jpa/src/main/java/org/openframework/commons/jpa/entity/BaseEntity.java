package org.openframework.commons.jpa.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY", updatable = false)
	private User createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", length = 19, updatable = false)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFIED_BY")
	private User modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", length = 19)
	private Date modifiedDate;

	// https://www.baeldung.com/jpa-default-column-values
	@Column(name = "IS_VALID", columnDefinition="bit default 1", nullable = false)
	private Boolean isValid;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[id="+this.getId()+toStringSub()+"]";
	}

	public String toStringSub() {
		return StringUtils.EMPTY;
	}
}
