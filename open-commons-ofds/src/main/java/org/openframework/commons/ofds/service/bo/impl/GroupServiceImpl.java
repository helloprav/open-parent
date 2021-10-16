/**
 * 
 */
package org.openframework.commons.ofds.service.bo.impl;

import java.util.List;

import javax.inject.Inject;

import org.openframework.commons.spring.Pagination;
import org.openframework.commons.cache.GlobalCacheApp;
import org.openframework.commons.cache.props.CommonsCacheProperties;
import org.openframework.commons.ofds.entity.Group;
import org.openframework.commons.ofds.service.adaptor.GroupAdaptor;
import org.openframework.commons.ofds.service.as.GroupAS;
import org.openframework.commons.ofds.service.bo.GroupService;
import org.openframework.commons.ofds.vo.GroupHistoryVO;
import org.openframework.commons.ofds.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Java Developer
 *
 */
@Service
@CacheConfig(cacheNames = GroupServiceImpl.GROUP_CACHE, cacheManager = GlobalCacheApp.caffeineCacheManager)
//@Cacheable(cacheNames = GROUP_CACHE, key = "#id", cacheManager = GlobalCacheApp.caffeineCacheManager)
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackForClassName = "Exception", isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class GroupServiceImpl extends BaseServiceImpl implements GroupService {

	protected static final String GROUP_CACHE = "group-cache";

	@Inject
	private GroupAS groupAS;

	@Inject
	private GroupAdaptor groupAdaptor;

	@Autowired
	private CommonsCacheProperties commonsCacheProperties;

	@Override
	public List<GroupVO> findGroups() {
		return groupAdaptor.toVO(groupAS.findGroups());
	}

	@Override
	public List<GroupVO> findGroupsByStatus(Boolean status, Pagination pagingDetails) {
		return groupAdaptor.toVO(groupAS.findGroupsByStatus(status, pagingDetails));
	}

	@Override
	@Cacheable(key = "#id")
	public GroupVO findGroupById(Long id) {
		System.out.println("id: "+id);
		System.out.println(commonsCacheProperties);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return groupAdaptor.toVO(groupAS.findGroupByIdWithFunctions(id), true);
	}

	@Override
	public List<GroupHistoryVO> findGroupHistoryById(Long id) {
		return groupAdaptor.toVOHistory(groupAS.findGroupHistoryById(id));
	}

	@Override
	public Long createGroup(GroupVO groupVO) {

		Group groupTobeCreated = groupAdaptor.fromVO(groupVO);
		groupAS.checkUniqueGroupName(groupTobeCreated.getGroupName());

		// set common properties
		setCommonProperties(groupTobeCreated, groupVO.getLoggedInUserId(), true);
		return groupAdaptor.toVO(groupAS.saveGroup(groupTobeCreated)).getId();
	}

	@Override
	@CachePut(key = "#groupVO.id")
	public GroupVO updateGroup(GroupVO groupVO) {

		Group groupTobeUpdated = groupAdaptor.fromVO(groupVO);
		groupAS.findGroupById(groupTobeUpdated.getId());
		groupAS.checkUniqueGroupNameNotId(groupTobeUpdated);

		// set common properties
		setCommonProperties(groupTobeUpdated, groupVO.getLoggedInUserId(), false);

		// delete existing mapping of groupFunctions
		groupAS.deleteGroupFunctions(groupTobeUpdated.getId());
		return groupAdaptor.toVO(groupAS.saveGroup(groupTobeUpdated));
	}

	@Override
	public GroupVO updateStatus(GroupVO groupVO) {

		Group groupTobeUpdated = groupAdaptor.fromVO(groupVO);

		// set common properties
		setCommonProperties(groupTobeUpdated, groupVO.getLoggedInUserId(), false);

		return groupAdaptor.toVO(groupAS.updateStatus(groupTobeUpdated));
	}

	@Override
	public void deleteGroup(GroupVO groupVO) {

		groupAS.deleteGroupFunctions(groupVO.getId());
		groupAS.deleteGroup(groupVO.getId());
	}
}
