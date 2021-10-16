/**
 * 
 */
package org.openframework.commons.rest.vo;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @author Java Developer
 *
 */
public class UserVO extends MainVO {

	private Long id;

	@NotNull(message = "first name is required")
	@Length(min=1, max=50, message="first name length should be between 5 and 50 characters")
	private String firstName;
	@Length(max=50, message="length should not be more than 50 characters")
	private String lastName;
	@Length(max=50, message="length should not be more than 50 characters")
	private String username;
	private char[] password;
	private String gender;
	@Email(message="email should be an valid email")
	@Length(max=50, message="length should not be more than 50 characters")
	private String email;
	@Length(max=50, message="length should not be more than 50 characters")
	private String mobile;
	@Length(max=50, message="length should not be more than 50 characters")
	private String phone;
	@Length(max=500, message="length should not be more than 500 characters")
	private String description;
	@Length(max=50, message="length should not be more than 50 characters")
	private String status;
	private Boolean isSuperAdmin;

	private Map<String, Object> otherData;

	public UserVO() {	}

	public UserVO(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long userID) {
		this.id = userID;
	}
	public String getFirstName() {
		return firstName;
	}
	public UserVO setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	public String getLastName() {
		return lastName;
	}
	public UserVO setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public UserVO setGender(String gender) {
		this.gender = gender;
		return this;
	}
	public String getEmail() {
		return email;
	}
	public UserVO setEmail(String email) {
		this.email = email;
		return this;
	}
	public String getMobile() {
		return mobile;
	}
	public UserVO setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}
	public String getPhone() {
		return phone;
	}
	public UserVO setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public UserVO setDescription(String desscription) {
		this.description = desscription;
		return this;
	}
	public String getStatus() {
		return status;
	}
	public UserVO setStatus(String status) {
		this.status = status;
		return this;
	}
	/*
	 * public String getRole() { return role; } public void setRole(String role) {
	 * this.role = role; }
	 */

	public Map<String, Object> getOtherData() {
		return otherData;
	}
	public UserVO setOtherData(Map<String, Object> otherData) {
		this.otherData = otherData;
		return this;
	}
	public UserVO addOtherData(String key, Object otherData) {
		if(null == this.otherData) {
			this.otherData = new HashMap<>();
		}
		this.otherData.put(key, otherData);
		return this;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"[id="+this.getId()+", email="+getEmail()+"]";
	}

	public String getUsername() {
		return username;
	}

	public UserVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public UserVO setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
		return this;
	}
}
