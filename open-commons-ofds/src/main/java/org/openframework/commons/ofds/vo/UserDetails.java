/**
 * 
 */
package org.openframework.commons.ofds.vo;

import org.openframework.commons.rest.vo.MainVO;
import org.openframework.commons.rest.vo.UserVO;

/**
 * @author Java Developer
 *
 */
public class UserDetails extends MainVO {

	private UserVO userVO;
	private UserAccess userAccess;

	public UserVO getUserVO() {
		return userVO;
	}
	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}
	public UserAccess getUserAccess() {
		return userAccess;
	}
	public void setUserAccess(UserAccess userAccess) {
		this.userAccess = userAccess;
	}
}
