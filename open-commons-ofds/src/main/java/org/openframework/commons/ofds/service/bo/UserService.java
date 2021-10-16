/**
 * 
 */
package org.openframework.commons.ofds.service.bo;

import org.openframework.commons.rest.vo.UserHistoryVO;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.PageList;
import org.openframework.commons.spring.Pagination;

/**
 * @author Java Developer
 *
 */
public interface UserService {

	PageList<UserVO> findUsers(Pagination pagingDetails);

	PageList<UserVO> findUsersByStatus(boolean isValid, Pagination pagingDetails);

	UserVO findUserById(Long id);

	PageList<UserHistoryVO> findUserHistoryById(Long userId, Pagination pagingDetails);

	Long createUser(UserVO userVO);

	UserVO updateUser(UserVO userVO);

	UserVO updateStatus(UserVO userVO);

	void deleteUser(UserVO userVO);

}
