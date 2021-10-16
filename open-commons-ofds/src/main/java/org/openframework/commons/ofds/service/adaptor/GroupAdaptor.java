package org.openframework.commons.ofds.service.adaptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.openframework.commons.enums.Action;
import org.openframework.commons.ofds.entity.Function;
import org.openframework.commons.ofds.entity.Group;
import org.openframework.commons.ofds.entity.GroupFunction;
import org.openframework.commons.ofds.entity.GroupHistory;
import org.openframework.commons.ofds.utils.EnumUtility;
import org.openframework.commons.ofds.vo.FunctionVO;
import org.openframework.commons.ofds.vo.GroupHistoryVO;
import org.openframework.commons.ofds.vo.GroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GroupAdaptor extends BaseAdaptor {

	public List<GroupVO> toVO(List<Group> groupList) {

		List<GroupVO> productCategoryVOList = new ArrayList<>();
		ListIterator<Group> listIterator = groupList.listIterator();
		while (listIterator.hasNext()) {
			Group group = listIterator.next();
			GroupVO productCategoryVO = toVO(group);
			if(null != productCategoryVO) {
				productCategoryVOList.add(productCategoryVO);
			}
		}
		return productCategoryVOList;
	}

	public GroupVO toVO(Group group) {
		return toVO(group, false);
	}

	public GroupVO toVO(Group group, boolean includeGroupFunctions) {
		return toVO(group, includeGroupFunctions, null);
	}

	public GroupVO toVO(Group group, boolean includeGroupFunctions, GroupVO groupVO) {

		if(null == group) {
			return null;
		}
		if(null == groupVO) {
			groupVO = new GroupVO();
		}
		BeanUtils.copyProperties(group, groupVO, "groupFunctions");
		// manually update modifiedBy as it is not automatically updated using BeanUtils.copyProperties
		toVOCheckerMaker(group, groupVO);
		if(includeGroupFunctions) {
			toFunctionVO(group.getGroupFunctions(), groupVO);
		}
		return groupVO;
	}

	private void toFunctionVO(List<GroupFunction> groupFunctions, GroupVO groupVO) {

		if(null == groupFunctions) {
			return;
		}

		List<FunctionVO> functionVOList = new ArrayList<>();
		Iterator<GroupFunction> iterator = groupFunctions.iterator();
		while (iterator.hasNext()) {
			GroupFunction groupFunction = iterator.next();
			FunctionVO functionVO = null;
			if(null!=groupFunction.getFunction() && groupFunction.getFunction().getIsValid()) {
				functionVO = new FunctionVO();
				functionVO.setId(groupFunction.getFunction().getId());
				functionVO.setName(groupFunction.getFunction().getName());
			}
			if(null != functionVO) {
				functionVOList.add(functionVO);
			}
		}
		groupVO.setFunctionList(functionVOList);
	}

	public List<GroupHistoryVO> toVOHistory(List<GroupHistory> groupHistoryList) {

		List<GroupHistoryVO> groupHistoryVOList = new ArrayList<>();
		ListIterator<GroupHistory> listIterator = groupHistoryList.listIterator();
		while (listIterator.hasNext()) {
			GroupHistory groupHistory = listIterator.next();
			GroupHistoryVO groupHistoryVO = toHistoryVO(groupHistory);
			updateFromEnumTypesToVO(groupHistory, groupHistoryVO);
			groupHistoryVOList.add(groupHistoryVO);
		}
		return groupHistoryVOList;
	}

	private void updateFromEnumTypesToVO(GroupHistory groupHistory, GroupHistoryVO groupHistoryVO) {

		logger.debug(EnumUtility.getEnumString(Action.class, groupHistory.getAction()));
		groupHistoryVO.setAction(groupHistory.getAction() == null ? null : groupHistory.getAction().toString());
	}

	public GroupHistoryVO toHistoryVO(GroupHistory groupHistory) {

		return toHistoryVO(groupHistory, false);
	}

	public GroupHistoryVO toHistoryVO(GroupHistory groupHistory, boolean includeGroupFunctions) {

		return toHistoryVO(groupHistory, includeGroupFunctions, new GroupHistoryVO());
	}

	public GroupHistoryVO toHistoryVO(GroupHistory groupHistory, boolean includeGroupFunctions, GroupHistoryVO groupHistoryVO) {

		if(null == groupHistory) {
			return null;
		}
		if(null == groupHistoryVO) {
			groupHistoryVO = new GroupHistoryVO();
		}
		BeanUtils.copyProperties(groupHistory, groupHistoryVO);
		// manually update modifiedBy as it is not automatically updated using BeanUtils.copyProperties
		toVOCheckerMaker(groupHistory, groupHistoryVO);
		if(includeGroupFunctions) {
			//toFunctionVO(groupHistory.getGroupFunctions(), groupHistoryVO);
		}
		return groupHistoryVO;
	}

	/**
	 * Populates the properties from groupVO to group entity. Apart from matching
	 * properties, it also populates the properties like createdBy.
	 * 
	 * @param groupVO
	 * @return populated group entity
	 */
	public Group fromVO(GroupVO groupVO) {

		Group group = new Group();
		BeanUtils.copyProperties(groupVO, group);
		fromFunctionVOList(groupVO.getFunctionList(), group);
		return group;
	}

	private void fromFunctionVOList(List<FunctionVO> functionVOList, Group group) {

		if(null == functionVOList) {
			return;
		}
		List<GroupFunction> groupFunctionList = new ArrayList<>();
		Iterator<FunctionVO> iterator = functionVOList.iterator();
		while (iterator.hasNext()) {

			FunctionVO functionVO = iterator.next();
			GroupFunction groupFunction = new GroupFunction();
			Function function = new Function();
			function.setId(functionVO.getId());
			groupFunction.setFunction(function);
			groupFunction.setGroup(group);
			groupFunctionList.add(groupFunction);
		}
		group.setGroupFunctions(groupFunctionList);
	}

}
