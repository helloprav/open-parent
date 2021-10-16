/**
 * 
 */
package org.openframework.commons.ofds.service.bo;

import java.util.List;

import org.openframework.commons.spring.Pagination;
import org.openframework.commons.ofds.vo.GroupHistoryVO;
import org.openframework.commons.ofds.vo.GroupVO;

/**
 * @author Java Developer
 *
 */
public interface GroupService {

	List<GroupVO> findGroups();

	List<GroupVO> findGroupsByStatus(Boolean status, Pagination pagingDetails);

	GroupVO findGroupById(Long id);

	List<GroupHistoryVO> findGroupHistoryById(Long id);

	Long createGroup(GroupVO groupVO);

	GroupVO updateGroup(GroupVO group);

	GroupVO updateStatus(GroupVO groupVO);

	void deleteGroup(GroupVO groupVO);
}
