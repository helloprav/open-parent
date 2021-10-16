package org.openframework.commons.ofds.service.as.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.openframework.commons.domain.exceptions.EntityConflictsException;
import org.openframework.commons.domain.exceptions.EntityNotFoundException;
import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.entity.Group;
import org.openframework.commons.ofds.entity.GroupFunction;
import org.openframework.commons.ofds.entity.GroupHistory;
import org.openframework.commons.ofds.service.as.GroupAS;
import org.openframework.commons.ofds.service.repository.GroupFunctionRepository;
import org.openframework.commons.ofds.service.repository.GroupHistoryRepository;
import org.openframework.commons.ofds.service.repository.GroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GroupASImpl extends BaseASImpl implements GroupAS {

	@Inject
	private GroupRepository groupRepository;

	@Inject
	private GroupHistoryRepository groupHistoryRepository;

	@Inject
	private GroupFunctionRepository groupFunctionRepository;

	@Override
	public List<Group> findGroups() {
		return groupRepository.findAll();
	}

	@Override
	public List<Group> findGroupsByStatus(Boolean status, Pagination pagingDetails) {
		Pageable paging = PageRequest.of(pagingDetails.getPage(), pagingDetails.getLimit());
		Page<Group> pagedResult = groupRepository.findByIsValid(status, paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<>();
		}
		// return groupRepository.findByIsValid(status);
	}

	@Override
	public Group findGroupById(Long id) {
		return returnIfEntityExists(id, groupRepository, false);
	}

	@Override
	public List<GroupHistory> findGroupHistoryById(Long id) {

		return groupHistoryRepository.findByParentId(id);
	}

	@Override
	public Group findGroupByIdWithFunctions(Long id) {
		return returnIfEntityExists(id, groupRepository, true);
	}

	@Override
	public Group saveGroup(Group inputGroup) {

		boolean isUpdate = null != inputGroup.getId();
		Group savedGroup = groupRepository.save(inputGroup);
		if(isUpdate) {
			Group refreshedGroup = groupRepository.findById(savedGroup.getId()).get();
			savedGroup = refreshedGroup;
		}
		List<GroupFunction> inputGroupFunctionsList = inputGroup.getGroupFunctions();
		List<GroupFunction> savedGroupFunctionsList = new ArrayList<>();
		if(null != inputGroupFunctionsList) {
			for (GroupFunction groupFunctions : inputGroupFunctionsList) {
				groupFunctions.setGroup(savedGroup);
				GroupFunction savedGroupFunctions = groupFunctionRepository.save(groupFunctions);
				savedGroupFunctionsList.add(savedGroupFunctions);
			}
		}
		savedGroup.setGroupFunctions(savedGroupFunctionsList);
		return savedGroup;
	}

	@Override
	public void checkUniqueGroupName(String groupName) {

		Group groupInDB = groupRepository.findByGroupName(groupName);
		if (null != groupInDB) {
			throw new EntityConflictsException(String.format("Requested group name [%s] already exists", groupName));
		}
	}

	/**
	 * Returns true if there is no group name conflict. Other wise throws exception.
	 * 
	 * @param groupName
	 * @param id
	 */
	@Override
	public void checkUniqueGroupNameNotId(Group inputGroup) {

		Group groupInDB = groupRepository.findByGroupName(inputGroup.getGroupName());
		if (null != groupInDB && null != groupInDB.getId() && !groupInDB.getId().equals(inputGroup.getId())) {
			throw new EntityConflictsException(
					String.format("Requested group name [%s] already exists with Id [%d]", inputGroup.getGroupName(),
							groupInDB.getId()));
		}
	}

	@Override
	public Group updateStatus(Group groupVO) {

		Integer count = groupRepository.updateStatus(groupVO.getId(), groupVO.getIsValid(), groupVO.getModifiedBy().getId(), groupVO.getModifiedDate());
		if(count==0) {
			throw new EntityNotFoundException(String.format("Requested entity %s not found", groupVO.getId()));
		}
		return groupVO;
	}

	@Override
	public void deleteGroupFunctions(Long id) {

		groupFunctionRepository.deleteByGroupID(id);
	}

	@Override
	public void deleteGroup(Long id) {

		Optional<Group> group = groupRepository.findById(id);
		if (group.isPresent()) {
			groupRepository.deleteById(id);
		}
	}
}
