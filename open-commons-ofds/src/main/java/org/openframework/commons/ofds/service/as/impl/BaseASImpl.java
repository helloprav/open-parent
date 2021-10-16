package org.openframework.commons.ofds.service.as.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.ofds.entity.Group;
import org.openframework.commons.ofds.entity.User;
import org.openframework.commons.ofds.service.repository.GroupRepository;
import org.openframework.commons.ofds.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseASImpl {

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	protected EntityManager em;

	public User returnIfEntityExists(Long id, UserRepository userRepository, boolean withDependency) {

		User user = null;
		if(withDependency) {
			List<User> userList = userRepository.findByIdWithGroups(id);
			if(!userList.isEmpty()) {
				user = userList.get(0);
			}
		} else {
			Optional<User> userOptional = userRepository.findById(id);
			if (!userOptional.isPresent()) {
				user = userOptional.get();
			}
		}
		if(null == user) {
			throw new EntityNotFoundException(String.format("Requested entity %s not found", id));
		} else {
			return user;
		}
	}

	public Group returnIfEntityExists(Long id, GroupRepository groupRepository, boolean withDependency) {

		Group group = null;
		if(withDependency) {
			List<Group> groupList = groupRepository.findByIdWithFunctions(id);
			if(!groupList.isEmpty()) {
				group = groupList.get(0);
			}
		} else {
			Optional<Group> groupOptional = groupRepository.findById(id);
			if(groupOptional.isPresent()) {
				group = groupOptional.get();
			}
		}
		if (null == group) {
			throw new EntityNotFoundException(String.format("Requested entity %s not found", id));
		} else {
			return group;
		}
	}

}
