/**
 * 
 */
package org.openframework.commons.ofds.service.bo.impl;

import javax.inject.Inject;

import org.openframework.commons.rest.vo.UserHistoryVO;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.PageList;
import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.entity.User;
import org.openframework.commons.ofds.service.adaptor.UserAdaptor;
import org.openframework.commons.ofds.service.as.UserAS;
import org.openframework.commons.ofds.service.bo.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Java Developer
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackForClassName = "Exception", isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Inject
	private UserAS userAS;

	@Inject
	private UserAdaptor userAdaptor;

	@Override
	public PageList<UserVO> findUsers(Pagination pagingDetails) {

		return userAdaptor.toPageList(userAS.findUsers(pagingDetails));
	}

	@Override
	public PageList<UserVO> findUsersByStatus(boolean isValid, Pagination pagingDetails) {
		return userAdaptor.toPageList(userAS.findUsersByStatus(isValid, pagingDetails));
	}

	@Override
	public UserVO findUserById(Long id) {
		return userAdaptor.toVO(userAS.findUserById(id), true);
	}

	@Override
	public PageList<UserHistoryVO> findUserHistoryById(Long userId, Pagination pagingDetails) {
		return userAdaptor.toHistoryPageList(userAS.findUserHistoryById(userId, pagingDetails));
	}

	@Override
	public Long createUser(UserVO userVO) {

		User userTobeCreated = userAdaptor.fromVO(userVO);
		userAS.checkUniqueUsername(userTobeCreated.getUsername());

		// set common properties
		setCommonProperties(userTobeCreated, userVO.getLoggedInUserId(), true);
		return userAdaptor.toVO(userAS.saveUser(userTobeCreated)).getId();
	}

	@Override
	public UserVO updateUser(UserVO userVO) {

		// check if userId exists

		User userTobeUpdated = userAdaptor.fromVO(userVO);
		userAS.findUserById(userVO.getId());
		userAS.checkUniqueUsernameNotId(userTobeUpdated);

		// set common properties
		setCommonProperties(userTobeUpdated, userVO.getLoggedInUserId(), false);

		// delete existing mapping of userGroups
		userAS.deleteUserGroups(userTobeUpdated.getId());
		return userAdaptor.toVO(userAS.saveUser(userTobeUpdated));
	}

	@Override
	public UserVO updateStatus(UserVO userVO) {

		User userTobeUpdated = userAdaptor.fromVO(userVO);

		// set common properties
		setCommonProperties(userTobeUpdated, userVO.getLoggedInUserId(), false);

		return userAdaptor.toVO(userAS.updateStatus(userTobeUpdated));
	}

	@Override
	public void deleteUser(UserVO userVO) {

		userAS.deleteUserGroups(userVO.getId());
		userAS.deleteUser(userVO.getId());
	}

}
