package org.openframework.commons.ofds.service.as;

import java.util.List;

import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.entity.Group;
import org.openframework.commons.ofds.entity.GroupHistory;

public interface GroupAS {

	public List<Group> findGroups();
	public List<Group> findGroupsByStatus(Boolean status, Pagination pagingDetails);

	public Group findGroupById(Long id);
	public List<GroupHistory> findGroupHistoryById(Long id);

	public Group findGroupByIdWithFunctions(Long id);

	public Group saveGroup(Group group);

	/**
	 * validates if the given groupName doesn't exists in db. Other wise throws exception.
	 * 
	 * @param groupName
	 * @param id
	 */
	public void checkUniqueGroupName(String groupName);

	/**
	 * Returns true if there is no group name conflict. Other wise throws exception.
	 * 
	 * @param groupName
	 * @param id
	 */
	public void checkUniqueGroupNameNotId(Group inputGroup);

	/**
	 * Updates the status(IsValid: active/inActive) of group
	 * @param fromVO
	 * @return updated status
	 */
	public Group updateStatus(Group fromVO);

	/**
	 * Deletes all the GroupFunction for the given group id
	 * 
	 * @param id
	 *            group id
	 */
	public void deleteGroupFunctions(Long id);

	public void deleteGroup(Long id);
}
