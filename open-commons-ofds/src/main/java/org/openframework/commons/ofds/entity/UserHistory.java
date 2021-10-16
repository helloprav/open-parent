package org.openframework.commons.ofds.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.openframework.commons.enums.Gender;
import org.openframework.commons.enums.UserStatus;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
//@DynamicUpdate
@Table(name = "UserHistory")
@NamedQuery(name = "UserHistory.findAll", query = "SELECT u FROM UserHistory u")
public class UserHistory extends HistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "FIRST_NAME", length = 50, nullable = false)
	private String firstName;

	@Column(name = "LAST_NAME", length = 50)
	private String lastName;
	
	@Column(name = "USERNAME", length = 50)
	private String username;

	@Column(name = "PASSWORD", length = 50)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "GENDER", length = 50)
	private Gender gender;

	@Column(name = "EMAIL", length = 50)
	private String email;

	@Column(name = "MOBILE", length = 50)
	private String mobile;

	@Column(name = "PHONE", length = 50)
	private String phone;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 50)
	private UserStatus status;

	@Column(name = "IS_SUPER_ADMIN")
	private Boolean isSuperAdmin;

	public UserHistory() {
		// no argument constructor
	}

	public UserHistory(Long id) {
		this.setId(id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}


}