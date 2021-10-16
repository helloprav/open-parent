package org.openframework.commons.ofds.service.as.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.openframework.commons.constants.NumberConstants;
import org.openframework.commons.domain.exceptions.EntityConflictsException;
import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.spring.Pagination;
import org.openframework.commons.spring.data.jpa.PaginationAdaptor;
import org.openframework.commons.ofds.entity.User;
import org.openframework.commons.ofds.entity.UserGroup;
import org.openframework.commons.ofds.entity.UserHistory;
import org.openframework.commons.ofds.service.as.UserAS;
import org.openframework.commons.ofds.service.repository.UserGroupRepository;
import org.openframework.commons.ofds.service.repository.UserHistoryRepository;
import org.openframework.commons.ofds.service.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserASImpl extends BaseASImpl implements UserAS {

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserHistoryRepository userHistoryRepository;

	@Inject
	private UserGroupRepository userGroupRepository;

	@Override
	public Page<User> findUsers(Pagination paginationVO) {

		Pageable paging = PaginationAdaptor.fromPaginationVO(paginationVO);
		if(null == paginationVO.getFilter()) {
			return userRepository.findAll(paging);
		} else {
			// create criteria query
			
			return userRepository.findAll(paging);
		}
	}

	@Override
	public Page<User> findUsersByStatus(boolean isValid, Pagination pagingDetails) {

		Pageable paging = PaginationAdaptor.fromPaginationVO(pagingDetails);
		if(isValid) {
			return userRepository.findByIsValidTrue(paging);
		}
		return userRepository.findByIsValid(isValid, paging);
	}

	@Override
	public User findUserById(Long id) {

		return returnIfEntityExists(id, userRepository, true);
	}

	@Override
	public User findUserByEmail(String email) {

		List<User> users = userRepository.findByEmail(email);
		if(users.isEmpty()) {
			return null;
		} else {
			return users.get(NumberConstants.INT_ZERO);
		}
	}

	@Override
	public User findUserByUsernameOrEmailOrMobile(User user) {

		List<User> users = userRepository.findByUsernameOrEmailOrMobile(user.getEmail(), user.getUsername(), user.getMobile());
		if(users.isEmpty()) {
			return null;
		} else if(users.size()>1) {
			logger.error("Multiple users found for the user [{}]", user);
			return null;
		} else {
			return users.get(NumberConstants.INT_ZERO);
		}
	}

	@Override
	public Page<UserHistory> findUserHistoryById(Long userId, Pagination pagingDetails) {

		Pageable paging = PaginationAdaptor.fromPaginationVO(pagingDetails);
		return userHistoryRepository.findByParentId(userId, paging);
	}

	/**
	 * userRepository.existsUserByUsername(username) can also be used. but it doesn't return the user id.
	 */
	@Override
	public void checkUniqueUsername(String username) {

		List<User> users = userRepository.usernameExists(username);
		if (!users.isEmpty()) {
			throw new EntityConflictsException(String.format("Requested username %s already register for User %d",
					username, users.get(0).getId()));
		}
	}

	@Override
	public void checkUniqueUsernameNotId(User user) {

		List<User> users = userRepository.usernameExists(user.getUsername(), user.getId());
		if (!users.isEmpty()) {
			throw new EntityConflictsException(String.format("Requested username %s already register for User %d",
					user.getUsername(), users.get(0).getId()));
		}
	}

	@Override
	public User findUserGroupAndAccess(User user) {

		return userRepository.findUserGroupsAndFunctions(user.getId());
	}

	@Override
	public User saveUser(User inputUser) {

		// don't update password, so take the latest password from db and set in current inputUser
		if(null != inputUser.getId()) {
			inputUser.setPassword(userRepository.getOne(inputUser.getId()).getPassword());
		}
		User savedUser = userRepository.save(inputUser);
		List<UserGroup> userGroupsList = inputUser.getUserGroups();
	
		if(null == userGroupsList) {
			return savedUser;
		}

		// save the user group mappings
		List<UserGroup> updatedUserGroupsList = new ArrayList<>();
		for (UserGroup userGroup : userGroupsList) {
			userGroup.setUser(savedUser);
			UserGroup savedUserGroups = userGroupRepository.save(userGroup);
			updatedUserGroupsList.add(savedUserGroups);
		}
		savedUser.setUserGroups(null);
		savedUser.setUserGroups(updatedUserGroupsList);
		return savedUser;
	}

	@Override
	public User updateStatus(User user) {

		Integer count = userRepository.updateStatus(user.getId(), user.getIsValid(), user.getModifiedBy().getId(), user.getModifiedDate());
		if(count==0) {
			throw new EntityNotFoundException(String.format("Requested entity %s not found", user.getId()));
		}
		return user;
	}

	@Override
	public void deleteUserGroups(Long userId) {

		userGroupRepository.deleteByUserID(userId);
	}

	@Override
	public void deleteUser(Long id) {

		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			userRepository.deleteById(id);
		}
	}

}
