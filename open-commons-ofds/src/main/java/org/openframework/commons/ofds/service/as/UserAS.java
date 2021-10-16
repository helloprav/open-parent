package org.openframework.commons.ofds.service.as;

import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.entity.User;
import org.openframework.commons.ofds.entity.UserHistory;
import org.springframework.data.domain.Page;

public interface UserAS {

	Page<User> findUsers(Pagination pagingDetails);
	Page<User> findUsersByStatus(boolean isValid, Pagination pagingDetails);

	User findUserById(Long id);
	User findUserByEmail(String email);
	User findUserByUsernameOrEmailOrMobile(User user);

	Page<UserHistory> findUserHistoryById(Long userId, Pagination pagingDetails);

	/**
	 * validates if the given username doesn't exists in db. Other wise throws exception.
	 * 
	 * @param username
	 */
	public void checkUniqueUsername(String username);

	/**
	 * Returns true if there is no username conflict. Other wise throws exception.
	 * 
	 * @param username
	 * @param id
	 */
	void checkUniqueUsernameNotId(User inputUser);

	User findUserGroupAndAccess(User user);

	User saveUser(User user);
	User updateStatus(User user);

	/**
	 * Deletes all the UserGroup for the given user id
	 * 
	 * @param userId
	 *            user id
	 */
	void deleteUserGroups(Long userId);

	void deleteUser(Long id);
}
