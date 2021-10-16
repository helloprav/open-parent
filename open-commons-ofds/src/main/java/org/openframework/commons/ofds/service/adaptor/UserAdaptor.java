package org.openframework.commons.ofds.service.adaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.openframework.commons.enums.Gender;
import org.openframework.commons.enums.UserStatus;
import org.openframework.commons.rest.vo.UserHistoryVO;
import org.openframework.commons.rest.vo.UserVO;
import org.openframework.commons.spring.PageList;
import org.openframework.commons.spring.data.jpa.PaginationAdaptor;
import org.openframework.commons.ofds.entity.Function;
import org.openframework.commons.ofds.entity.Group;
import org.openframework.commons.ofds.entity.GroupFunction;
import org.openframework.commons.ofds.entity.User;
import org.openframework.commons.ofds.entity.UserGroup;
import org.openframework.commons.ofds.entity.UserHistory;
import org.openframework.commons.ofds.utils.EnumUtility;
import org.openframework.commons.ofds.vo.GroupVO;
import org.openframework.commons.ofds.vo.UserCredentialsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserAdaptor extends BaseAdaptor {

	public static final String GROUP_LIST = "groupList";
	public static final String FUNCTION_LIST = "functionList";

	@Autowired
	private GroupAdaptor groupAdaptor;

	public PageList<UserVO> toPageList(Page<User> usersPage) {

		List<UserVO> userVOList = new ArrayList<>();
		ListIterator<User> listIterator = usersPage.getContent().listIterator();
		while (listIterator.hasNext()) {
			User user = listIterator.next();
			UserVO userVO = toVO(user);
			userVOList.add(userVO);
		}

		PageList<UserVO> pageList = new PageList<>();
		pageList.setData(userVOList);
		pageList.setPagination(PaginationAdaptor.toPaginationVO(usersPage.getPageable(), usersPage.getTotalElements(), userVOList.size()));
		return pageList;
	}

	public PageList<UserHistoryVO> toHistoryPageList(Page<UserHistory> usersPage) {

		List<UserHistoryVO> userHistoryVOList = new ArrayList<>();
		ListIterator<UserHistory> listIterator = usersPage.getContent().listIterator();
		while (listIterator.hasNext()) {
			UserHistory userHistory = listIterator.next();
			UserHistoryVO userHistoryVO = toHistoryVO(userHistory);
			userHistoryVOList.add(userHistoryVO);
		}

		PageList<UserHistoryVO> pageList = new PageList<>();
		pageList.setData(userHistoryVOList);
		pageList.setPagination(PaginationAdaptor.toPaginationVO(usersPage.getPageable(), usersPage.getTotalElements(), userHistoryVOList.size()));
		return pageList;
	}

	public List<UserVO> toVO(List<User> users) {

		List<UserVO> userVOList = new ArrayList<>();
		ListIterator<User> listIterator = users.listIterator();
		while (listIterator.hasNext()) {
			User user = listIterator.next();
			UserVO userVO = toVO(user);
			userVOList.add(userVO);
		}
		return userVOList;
	}

	public UserVO toVO(User user) {
		return toVO(user, false);
	}

	public UserVO toVO(User user, boolean includeUserGroups) {

		logger.debug("START:: {}", user.getId());
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(user, userVO);
		toVOCheckerMaker(user, userVO);
		updateFromEnumTypesToVO(user, userVO);
		if(includeUserGroups) {
			updateGroupAndFunction(user, userVO);
		}
		logger.debug("END:: {}", user.getId());
		return userVO;
	}

	public UserHistoryVO toHistoryVO(UserHistory userHistory) {

		logger.debug("START:: {}", userHistory.getId());
		UserHistoryVO userHistoryVO = new UserHistoryVO();
		BeanUtils.copyProperties(userHistory, userHistoryVO);
		toVOCheckerMaker(userHistory, userHistoryVO);
		updateFromEnumTypesToVO(userHistory, userHistoryVO);
		logger.debug("END:: {}", userHistory.getId());
		return userHistoryVO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateGroupAndFunction(User user, UserVO userVO) {

		List<GroupVO> groupList = new ArrayList<>();
		if (null != user.getUserGroups()) {
			List<UserGroup> userGroups = user.getUserGroups();
			ListIterator<UserGroup> iterator = userGroups.listIterator();
			while (iterator.hasNext()) {
				UserGroup userGroup = iterator.next();
				GroupVO groupVO = groupAdaptor.toVO(userGroup.getGroup());
				if(null != groupVO) {
					groupList.add(groupVO);
				}
			}
		}
		Map map = new HashMap<>();
		map.put(GROUP_LIST, groupList);
		userVO.setOtherData(map);
	}

	public User fromVO(UserVO userVO) {
		User user = new User();
		BeanUtils.copyProperties(userVO, user);

		// copy password
		if(null!=userVO.getPassword()) {
			Character[] passwd = new Character[userVO.getPassword().length];
			for(int i=0; i<userVO.getPassword().length; i++) {
				passwd[i] = Character.valueOf(userVO.getPassword()[i]);
			}
			user.setPassword(Arrays.toString(passwd));
		}

		updateEnumTypesFromVO(userVO, user);

		if(null == user.getIsSuperAdmin()) {
			user.setIsSuperAdmin(false);
		}
		if(null != userVO.getOtherData()) {
			@SuppressWarnings({ "unchecked" })
			List<Integer> groupIdList = (List<Integer>) userVO.getOtherData().get(GROUP_LIST);
			fromGroupVOList(groupIdList, user);
		}
		return user;
	}

	private void fromGroupVOList(List<Integer> groupIdList, User user) {

		if(null == groupIdList) {
			return ;
		}
		Set<Integer> groupIdSet = new HashSet<>(groupIdList);
		List<UserGroup> userGroupList = new ArrayList<>();
		Iterator<Integer> iterator = groupIdSet.iterator();
		while (iterator.hasNext()) {

			int id = iterator.next();

			UserGroup userGroup = new UserGroup();
			Group group = new Group();
			group.setId((long)id);

			userGroup.setGroup(group);
			userGroup.setUser(user);
			userGroupList.add(userGroup);
		}
		user.setUserGroups(userGroupList);
	}

	public User fromVO(UserCredentialsVO userCredentialVO) {
		User user = new User();
		user.setUsername(userCredentialVO.getUsername());
		return user;
	}

	private void updateEnumTypesFromVO(UserVO userVO, User user) {

		user.setGender((Gender) EnumUtility.getEnumConstant(Gender.class, userVO.getGender()));
		user.setStatus((UserStatus) EnumUtility.getEnumConstant(UserStatus.class, userVO.getStatus()));
	}

	private void updateFromEnumTypesToVO(User user, UserVO userVO) {

		logger.debug(EnumUtility.getEnumString(Gender.class, user.getGender()));
		userVO.setGender(user.getGender() == null ? null : user.getGender().toString());
		userVO.setStatus(user.getStatus() == null ? null : user.getStatus().toString());
	}

	private void updateFromEnumTypesToVO(UserHistory userHistory, UserHistoryVO userHistoryVO) {

		logger.debug(EnumUtility.getEnumString(Gender.class, userHistory.getGender()));
		userHistoryVO.setGender(userHistory.getGender() == null ? null : userHistory.getGender().toString());
		userHistoryVO.setStatus(userHistory.getStatus() == null ? null : userHistory.getStatus().toString());
	}

	public void populateUserAccess(User user, UserVO userVO) {

		Set<String> functionList = new TreeSet<>();
		if(null != user) {
			List<UserGroup> userGroups = user.getUserGroups();
			for(UserGroup userGroup: userGroups) {
				Group group = userGroup.getGroup();
				List<GroupFunction> groupFunctionList = group.getGroupFunctions();
				for(GroupFunction groupFunction: groupFunctionList) {
					Function function = groupFunction.getFunction();
					if(function.getIsValid()) {
						functionList.add(function.getName());
					}
				}
			}
		}
		/*
		 * Map<String, Object> otherData = userVO.getOtherData(); if(null == otherData)
		 * { otherData = new HashMap<>(); } otherData.put(FUNCTION_LIST, functionList);
		 * userVO.setOtherData(otherData);
		 */
		userVO.addOtherData(FUNCTION_LIST, functionList);
	}

	public void populateUserAccess(List<Function> functionList, UserVO userVO) {

		List<String> functionNameList = new ArrayList<String>();
		for(Function function: functionList) {
			functionNameList.add(function.getName());
		}
		userVO.addOtherData(FUNCTION_LIST, functionNameList);
	}

}
