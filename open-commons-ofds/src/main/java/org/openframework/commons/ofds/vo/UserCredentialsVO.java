/**
 * 
 */
package org.openframework.commons.ofds.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Java Developer
 *
 */
public class UserCredentialsVO {

	@NotNull(message = "Username is required")
	@Size(min=1, max=50, message="length should not be more than 50 characters")
	private String username;

	@NotNull(message = "Password is required")
	@Size(min=5, max=50, message="The length should be between 5 and 50 characters long")
	private char[] password;

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
